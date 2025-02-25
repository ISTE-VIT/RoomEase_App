package com.example.roomease.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.roomease.R
import com.example.roomease.domain.model.User
import com.example.roomease.network.HttpClientFactory
import com.example.roomease.network.constructUrl
import com.example.roomease.network.safeCall
import com.example.roomease.ui.viewmodel.UserViewModel
import com.example.roomease.utils.network.Result
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent

class GoogleSignInUtils : KoinComponent {

    companion object {
        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            login: () -> Unit,
            userViewModel: UserViewModel
        ) {
            val credentialManager = CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()

            scope.launch {
                try {
                    val result = credentialManager.getCredential(context, request)
                    when (result.credential) {
                        is CustomCredential -> {
                            if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential =
                                    GoogleAuthProvider.getCredential(googleTokenId, null)
                                val firebaseUser =
                                    Firebase.auth.signInWithCredential(authCredential).await().user
                                firebaseUser?.let {
                                    if (!it.isAnonymous) {
                                        // Retrieve the Firebase ID token
                                        val tokenResult = it.getIdToken(true).await()
                                        val idToken = tokenResult.token
                                        if (idToken != null) {
                                            // Send the token to the backend
                                            val sent = sendFirebaseTokenToBackend(idToken)
                                            if (sent) {
                                                val userDetails = User(
                                                    userId = it.uid,
                                                    username = it.displayName ?: "",
                                                    email = it.email,
                                                )
                                                // Store user information via the viewmodel.
                                                userViewModel.storeUser(userDetails) {
                                                    login.invoke()
                                                }
                                            } else {
                                                // Handle token sending error if needed.
                                            }
                                        } else {
                                            // If no token is available, continue without sending.
                                            login.invoke()
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            /* Handle other credential types if needed */
                        }
                    }
                } catch (e: NoCredentialException) {
                    launcher?.launch(getIntent())
                } catch (e: GetCredentialException) {
                    e.printStackTrace()
                }
            }
        }

        fun logout(context: Context) {
            // Clear stored user data
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            // Sign out from Firebase
            Firebase.auth.signOut()
            Log.d("GoogleSignInUtils", "Logged out")
        }

        private suspend fun sendFirebaseTokenToBackend(idToken: String): Boolean {
            // Send the token to the backend
            val client = HttpClientFactory.create(engine = CIO.create())
            val url = constructUrl("/auth/verifyToken")
            Log.d("GoogleSignInUtils", "Sending token to backend: $idToken")
            val result = safeCall<Unit> {
                client.post(url) {
                    header("idToken", idToken)
                }
            }
            if (result is Result.Success) {
                Log.d("GoogleSignInUtils", "Token sent successfully")
                return true
            } else {
                Log.e("GoogleSignInUtils", "Failed to send token")
                return false
            }
        }

        private fun getIntent(): Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }

        private fun getCredentialOptions(context: Context): CredentialOption {
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()
        }
    }
}