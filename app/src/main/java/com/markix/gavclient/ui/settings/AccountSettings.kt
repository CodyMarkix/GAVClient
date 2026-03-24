package com.markix.gavclient.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.utils.addCharAtIndex

@Composable
fun FossLibraryEntry(name: String, link: String) {
    Text(buildAnnotatedString {
        append("\u2022 ")
        withLink(
            LinkAnnotation.Url(
                link,
                TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary))
            )
        ) {
            append(name)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettings(navActions: NavActions, gaViewModel: GAVAPIViewModel) {
    val accountState = gaViewModel.accountInfo.collectAsState()
    var showOpenSourceModal by remember { mutableIntStateOf(0) }
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
                .padding(innerPadding)
                .fillMaxHeight(),
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
                    text = stringResource(R.string.accountsettings_opengooglesettings),
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
                    text = stringResource(R.string.accountsettings_signout),
                    fontSize = 18.sp
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = stringResource(R.string.foss_info),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.surfaceBright,
                modifier = Modifier
                    .padding(5.dp, 20.dp)
                    .clickable() {
                        showOpenSourceModal = 1
                    }
            )
        }
        if (showOpenSourceModal == 1) {
            AlertDialog(
                icon = {},
                title = {
                    Text("Open-source")
                },
                text = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = stringResource(R.string.foss_builtwithfoss),
                            modifier = Modifier
                                .padding(0.dp, 10.dp)
                        )

                        FossLibraryEntry("square/okhttp", "https://github.com/square/okhttp")
                        FossLibraryEntry("hub4j/github-api", "https://github.com/hub4j/github-api")
                        FossLibraryEntry("Jetpack (androidx/androidx)", "https://github.com/androidx/androidx/")
                    }
                },
                onDismissRequest = {
                    showOpenSourceModal = 0
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showOpenSourceModal = 0
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {}
            )
        }
    }
}