package com.markix.gavclient.logic.viewmodels

import android.app.Application
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
import androidx.lifecycle.AndroidViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.markix.gavclient.logic.data.ClassroomInfo
import com.markix.gavclient.logic.data.IOCAgendaData
import com.markix.gavclient.logic.data.IOCKeyword
import com.markix.gavclient.logic.data.IOCTopicData
import com.markix.gavclient.logic.data.net.UserResponse
import com.markix.gavclient.utils.NonSchoolAccountException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request


data class AccountData (
    var tokenId: String? = null,
    var accountFirstName: String? = null,
    var accountLastName: String? = null,
    var accountAvatarURI: Uri? = null,
    var accountClass: String? = null,
    var accountMail: String? = null,
    var accountApiID: Int? = null,
)

class GAVAPIViewModel(application: Application) : AndroidViewModel(application) {
    private val apiURL = "https://vyuka.gyarab.cz/api"
    private val client = OkHttpClient()
    private val _accountInfo = MutableStateFlow(AccountData())
    val accountInfo: StateFlow<AccountData> = _accountInfo.asStateFlow()

    suspend fun getAccountInfo(): UserResponse {
        val request = Request.Builder()
            .url("$apiURL/system/user")
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        return Json.decodeFromString<UserResponse>(response.body!!.string())
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

                            if (googleIdTokenCredential.id.contains("gyarab.cz")) {
                                _accountInfo.update { currentState ->
                                    currentState.copy(
                                        accountMail = googleIdTokenCredential.id,
                                        tokenId = googleIdTokenCredential.idToken,
                                        accountFirstName = googleIdTokenCredential.givenName,
                                        accountLastName = googleIdTokenCredential.familyName,
                                        accountAvatarURI = googleIdTokenCredential.profilePictureUri
                                    )
                                }
                                val gavUserInfo = getAccountInfo()
                                _accountInfo.update { currentState ->
                                    currentState.copy(
                                        accountClass = gavUserInfo.group
                                    )
                                }
                            } else {
                                throw NonSchoolAccountException("Vybraný účet není školní")
                            }
                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e(context.packageName, "Received an invalid google id token response", e)
                        } catch (e: NonSchoolAccountException) {
                            Log.e(context.packageName, e.message, e)
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
            Log.e(context.packageName, "$failureMessage: Failure getting credentials", e)
        } catch (e: GoogleIdTokenParsingException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(context.packageName,
                "$failureMessage: Issue with parsing received GoogleIdToken", e)
        } catch (e: NoCredentialException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, "$failureMessage: No credentials found", e)
            return e
        } catch (e: GetCredentialCustomException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, "$failureMessage: Issue with custom credential request", e)
        } catch (e: GetCredentialCancellationException) {
            Toast.makeText(context, ": Sign-in cancelled", Toast.LENGTH_SHORT).show()
            Log.e(context.packageName, "$failureMessage: Sign-in was cancelled", e)
        }
        return e
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

    suspend fun getClassroomInfo(): ClassroomInfo {
        val request = Request.Builder()
            .url("$apiURL/classroom/student")
            .header("Authorization", "Bearer ${_accountInfo.value.tokenId}")
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        return Json.decodeFromString<ClassroomInfo>(response.body!!.string())
    }

    suspend fun getIOCAgendas(): List<IOCAgendaData> {
        val request = Request.Builder()
            .url("$apiURL/thesis/agenda")
            .header("Authorization", "Bearer ${_accountInfo.value.tokenId}")
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        return Json.decodeFromString<List<IOCAgendaData>>(response.body!!.string())
    }

    suspend fun getIOCAgendaKeywords(agendaId: Int): List<IOCKeyword> {
        val request = Request.Builder()
            .url("$apiURL/thesis/agenda-keyword/${agendaId}")
            .header("Authorization", "Bearer ${_accountInfo.value.tokenId}")
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        return Json.decodeFromString<List<IOCKeyword>>(response.body!!.string())
    }

    suspend fun getIOCTopicKeywords(topicId: Int): List<Int> {
        val request = Request.Builder()
            .url("$apiURL/thesis/topic-keyword/${topicId}")
            .header("Authorization", "Bearer ${_accountInfo.value.tokenId}")
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        return Json.decodeFromString<List<Int>>(response.body!!.string())
    }

    suspend fun getIOCTopics(id: Int): List<IOCTopicData> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("$apiURL/thesis/topic/${id}")
            .header("Authorization", "Bearer ${_accountInfo.value.tokenId}")
            .build()

        client.newCall(request).execute().use { response ->
            Json.decodeFromString<List<IOCTopicData>>(response.body!!.string())
        }
    }
}