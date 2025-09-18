package com.markix.gavclient.settings

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun handleLoginFlow(activityContext: Context) {
    val credentialManager = CredentialManager.create(activityContext)

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId("247364244458-5vcrgade9mf5s708p2qnbs82gmljf80j.apps.googleusercontent.com")
        .setAutoSelectEnabled(false)
        // nonce string to use when generating a Google ID token
        .setNonce("nonce")
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    coroutineScope {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext,
            )
            Log.d("com.markix.gavclient", result.toString())
        } catch (e: GetCredentialException) {
            Log.d("com.markix.gavclient", "Login exception: " + e.stackTraceToString())
        }
    }


}

@Composable
fun LoginScreen(
    navigationController: NavController?,
    credentialManager: CredentialManager?,
    activityContext: Context,
    ) {
    val coroutineScope = rememberCoroutineScope()

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
            Button(
                onClick = {
                    Log.d("com.markix.gavclient", "Attempting to log in")
                    coroutineScope.launch {
                        handleLoginFlow(activityContext)
                    }
                },
                colors = ButtonColors(
                    containerColor = Color(0xffdcb486),
                    contentColor = Color(0xffffffff),
                    disabledContainerColor = Color(0xffdcb486),
                    disabledContentColor = Color(0xffffffff),
                )
            ) {
                Text("Přihlásit se pomocí služby Google")
            }
        }
    }
}