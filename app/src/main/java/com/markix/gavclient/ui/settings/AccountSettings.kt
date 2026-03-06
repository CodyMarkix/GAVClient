package com.markix.gavclient.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.NavDestinations
import com.markix.gavclient.R
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.utils.addCharAtIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettings(navActions: NavActions, gaViewModel: GAVAPIViewModel) {
    val accountState = gaViewModel.accountInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text("Gyarab Výuka")
                },
                actions = {

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(25.dp, 25.dp, 25.dp, 0.dp)
            ) {
                AsyncImage(
                    model = accountState.value.accountAvatarURI ?: painterResource(id = R.drawable.account_circle_24px),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = Modifier
                        .padding(25.dp, 20.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        text = (accountState.value.accountFirstName + " " + accountState.value.accountLastName) ?: "John Android",
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = accountState.value.accountClass?.addCharAtIndex('.', 1) ?: "null",
                        fontSize = 16.sp
                    )
                    Text(
                        text = accountState.value.accountMail ?: "name.surname@gyarab.cz",
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(25.dp, 60.dp, 0.dp, 0.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.tertiary,
                ),
                onClick = {
                }
            ) {
                Text(
                    text = "Otevřít nastavení Google",
                    fontSize = 18.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(25.dp, 10.dp, 0.dp, 0.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.tertiary,
                ),
                onClick = {
                    gaViewModel.signOut()
                    navActions.navigateToLoginScreen()
                }
            ) {
                Text(
                    text = "Odhlásit se",
                    fontSize = 18.sp
                )
            }
        }
    }
}