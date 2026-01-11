package com.markix.gavclient.logic.viewmodels

import androidx.lifecycle.AndroidViewModel;
import android.app.Application;
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCustomException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.markix.gavclient.logic.data.net.UserResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

data class accountData (
    var tokenId: String? = null,
    var accountFirstName: String? = null,
    var accountLastName: String? = null,
    var accountAvatarURI: Uri? = null,
    var accountClass: String? = null,
    var accountMail: String? = null,
    var accountApiID: Int? = null,
)

class GAVAPIViewModel(application: Application) : AndroidViewModel(application) {
    private val apiURL = "https://vyuka.gyarab.cz/api/";
    private val queue = Volley.newRequestQueue(application);

    private val _accountInfo = MutableStateFlow(accountData())
    val accountInfo: StateFlow<accountData> = _accountInfo.asStateFlow()

    fun getAccountInfo() {
        val request = StringRequest(
            Request.Method.GET,
            "$apiURL/system/user",
            { response ->
                val responseJson = Json.decodeFromString<UserResponse>(response);
                Log.d("com.markix.gavclient", "Response: $response");
                _accountInfo.update { currentState ->
                    currentState.copy(
                        accountClass = responseJson.group,
                        accountApiID = responseJson.id
                    )
                }
            },
            {
                Log.e("com.markix.gyarab", "Something went wrong trying to contact GAV API!");
            }

        )
    }

    suspend fun signIn(
        request: GetCredentialRequest,
        context: Context
    ): Exception? {
        val credentialManager = CredentialManager.create(context)
        val failureMessage = "Neúspěšný pokus přihlásit se!"
        var e: Exception? = null

        delay(25)
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            when (result.credential) {
                is CustomCredential -> {
                    if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            // Use googleIdTokenCredential and extract id to validate and
                            // authenticate on your server.
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(result.credential.data)
                            _accountInfo.update { currentState ->
                                currentState.copy(
                                    accountMail = googleIdTokenCredential.id,
                                    tokenId = googleIdTokenCredential.idToken,
                                    accountFirstName = googleIdTokenCredential.givenName,
                                    accountLastName = googleIdTokenCredential.familyName,
                                    accountAvatarURI = googleIdTokenCredential.profilePictureUri
                                )
                            }
                            getAccountInfo()

                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e(context.packageName, "Received an invalid google id token response", e)
                        }
                    } else {
                        // Catch any unrecognized credential type here.
                        Log.e(context.packageName, "Unexpected type of credential")
                    }
                }
                else -> {
                    // Catch any unrecognized credential type here.
                    Log.e(context.packageName, "Unexpected type of credential")
                }
            }

            Toast.makeText(context, "Sign in successful!", Toast.LENGTH_SHORT).show()
        } catch (e: GetCredentialException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, failureMessage + ": Failure getting credentials", e)
        } catch (e: GoogleIdTokenParsingException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, failureMessage + ": Issue with parsing received GoogleIdToken", e)
        } catch (e: NoCredentialException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, failureMessage + ": No credentials found", e)
            return e
        } catch (e: GetCredentialCustomException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, failureMessage + ": Issue with custom credential request", e)
        } catch (e: GetCredentialCancellationException) {
            Toast.makeText(context, ": Sign-in cancelled", Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, failureMessage + ": Sign-in was cancelled", e)
        }
        return e;
    }

    fun signOut() {
        _accountInfo.update { currentState ->
            currentState.copy(
                tokenId = null,
                accountFirstName = null,
                accountLastName = null,
                accountAvatarURI = null,
                accountClass = null,
                accountMail = null,
                accountApiID = null
            )
        }
    }
}