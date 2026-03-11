package com.markix.gavclient.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.markix.gavclient.R
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.ui.theme.AppTheme
import kotlin.system.exitProcess

@Composable
fun LoginSheet(
    GAViewModel: GAVAPIViewModel,
    credentialManager: CredentialManager,
    onSignIn: (e: Exception?) -> Unit,
) {
    val context = LocalContext.current
    var openAlertDialog by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(context.resources.getText(R.string.web_client_id) as String)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val e = GAViewModel.signIn(request, context, credentialManager)
        if (e is NoCredentialException) {
            val googleIdOptionFalse: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.resources.getText(R.string.web_client_id) as String)
                .build()

            val requestFalse: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOptionFalse)
                .build()

            val e = GAViewModel.signIn(requestFalse, context, credentialManager)

            if (e == null) {
                onSignIn(e)
            }
        }

        if (e == null) {
            onSignIn(e)
        } else {
            openAlertDialog++;
        }
    }

    if (openAlertDialog == 1) {
        AlertDialog(
            icon = {},
            title = {
                Text(text = stringResource(R.string.error_title))
            },
            text = {
                Text(text = stringResource(R.string.error_nosignin))
            },
            onDismissRequest = {
                exitProcess(0);
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        exitProcess(0);
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {}
        )
    }
}

@Composable
fun SigningInText() {
    Column(
        modifier = Modifier
            .padding(0.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.login_signingin))
        CircularProgressIndicator(
            modifier = Modifier.width(32.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDisplay() {
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
                text = stringResource(R.string.login_welcome)
            )
            SigningInText()
        }
    }
}
@Composable
fun LoginScreen(
    GAViewModel: GAVAPIViewModel,
    credentialManager: CredentialManager,
    onSignIn: (e: Exception?) -> Unit
) {
    LoginSheet(GAViewModel, credentialManager, onSignIn)
    LoginDisplay()
}

@Composable
@Preview
fun LoginScreenPreview() {
    AppTheme() {
        LoginDisplay()
    }
}