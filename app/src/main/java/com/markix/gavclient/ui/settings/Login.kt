package com.markix.gavclient.ui.settings

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.markix.gavclient.R
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.markix.gavclient.ui.theme.AppTheme

@Composable
fun LoginSheet(
    GAViewModel: GAVAPIViewModel,
    onSignIn: (e: Exception?) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(context.resources.getText(R.string.web_client_id) as String)
            .setNonce("transrights")
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val e = GAViewModel.signIn(request, context)
        if (e is NoCredentialException) {
            val googleIdOptionFalse: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.resources.getText(R.string.web_client_id) as String)
                .setNonce("transrights")
                .build()

            val requestFalse: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOptionFalse)
                .build()

            val e = GAViewModel.signIn(request, context)
        }

        if (e == null) {
            onSignIn(e)
        }
    }
}

@Composable
fun SignInButton(onSignIn: (e: Exception?) -> Unit, viewModel: GAVAPIViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Image(
        painter = painterResource(R.drawable.android_neutral_rd_si),
        contentDescription = "",
        modifier = Modifier.clickable {
            coroutineScope.launch {
                val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
                    .Builder(context.resources.getString(R.string.web_client_id))
                    .setNonce("transrights")
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(signInWithGoogleOption)
                    .build()

                var e = viewModel.signIn(request, context);

                if (e is NoCredentialException) {
                    Log.d("com.markix.gavclient", e.errorMessage.toString())
                }

                if (e == null) {
                    onSignIn(e)
                }
            }
        }
    )
}

@Composable
fun SigningInText() {
    Column(
        modifier = Modifier
            .padding(0.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Přihlašuji...")
        CircularProgressIndicator(
            modifier = Modifier.width(32.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

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
                text = "Vítejte v aplikaci Gyarab Výuka"
            )
            SigningInText()
        }
    }
}
@Composable
fun LoginScreen(
    GAViewModel: GAVAPIViewModel,
    onSignIn: (e: Exception?) -> Unit
) {
    LoginSheet(GAViewModel, onSignIn)
    LoginDisplay()
}

@Composable
@Preview
fun LoginScreenPreview() {
    AppTheme() {
        LoginDisplay()
    }
}