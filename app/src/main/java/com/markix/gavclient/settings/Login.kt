package com.markix.gavclient.settings

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCustomException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.markix.gavclient.R
import com.markix.gavclient.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun signIn(request: GetCredentialRequest, context: Context): Exception? {
    val credentialManager = CredentialManager.create(context)
    val failureMessage = "Neúspěšný pokus přihlásit se!"
    var e: Exception? = null

    delay(25)
    try {
       val result = credentialManager.getCredential(
           request = request,
           context = context
       )
       Toast.makeText(context, "Sign in successful!", Toast.LENGTH_SHORT).show()
    } catch (e: GetCredentialException) {
        Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
        Log.e("com.markix.gavclient", failureMessage + ": Failure getting credentials", e)
    } catch (e: GoogleIdTokenParsingException) {
        Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
        Log.e("com.markix.gavclient", failureMessage + ": Issue with parsing received GoogleIdToken", e)
    } catch (e: NoCredentialException) {
        Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
        Log.e("com.markix.gavclient", failureMessage + ": No credentials found", e)
        return e
    } catch (e: GetCredentialCustomException) {
        Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
        Log.e("com.markix.gavclient", failureMessage + ": Issue with custom credential request", e)
    } catch (e: GetCredentialCancellationException) {
        Toast.makeText(context, ": Sign-in cancelled", Toast.LENGTH_SHORT).show()
        Log.e("com.markix.gavclient", failureMessage + ": Sign-in was cancelled", e)
    }
    return e
}

@Composable
fun LoginSheet(onSignIn: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.d("com.markix.gavclient", "GETGOOGLEIDOPTION")
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(context.resources.getText(R.string.web_client_id) as String)
            .setNonce("transrights")
            .build()

        Log.d("com.markix.gavclient", "GETCREDENTIALREQUEST")
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        Log.d("com.markix.gavclient", "SIGNINATTEMPT")
        val e = signIn(request, context)
        if (e is NoCredentialException) {
            Log.d("com.markix.gavclient", "GETGOOGLEIDOPTION2")
            val googleIdOptionFalse: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.resources.getText(R.string.web_client_id) as String)
                .setNonce("transrights")
                .build()

            Log.d("com.markix.gavclient", "CREDENTIALREQUEST")
            val requestFalse: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOptionFalse)
                .build()

            Log.d("com.markix.gavclient", "SIGNINATTEMPT2")
            signIn(requestFalse, context)
        }

        Log.d("com.gyarab.vyuka", e.toString())
        if (e == null) {
            onSignIn()
        }
    }
}

@Composable
fun SignInButton(onSignIn: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(context.resources.getText(R.string.web_client_id) as String)
            .setNonce("transrights")
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        var e: Exception? = null
        coroutineScope.launch {
            e = signIn(request, context)
        }

        if (e == null) {
            onSignIn()
        }
    }
    Image(
        painter = painterResource(R.drawable.android_neutral_rd_si),
        contentDescription = "",
        modifier = Modifier
            .clickable(enabled = true, onClick = onClick)
    )
}

@Composable
fun LoginScreen(
    navigationController: NavController,
    onSignIn: () -> Unit
    ) {
    LoginSheet(onSignIn)

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = "Vítejte v aplikaci Gyarab Výuka"
            )
            SignInButton(onSignIn)
        }
    }
}