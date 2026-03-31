package com.markix.gavclient.ui.apps.storage

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import androidx.core.net.toUri
import com.markix.gavclient.ui.theme.AppTheme

@Composable
fun OpenGoogleDriveButton(context: Context) {
    Button(
        onClick = {
            val googleDriveIntent = Intent()
                .setClassName("com.google.android.apps.docs",  "com.google.android.apps.docs.app.NewMainProxyActivity")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val googlePlayPageIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .setData("market://details?id=com.google.android.apps.docs".toUri())
                .setPackage("com.android.vending")

            if (googleDriveIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(googleDriveIntent)
            } else if (googlePlayPageIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(googlePlayPageIntent)
            } else {
                val webIntent = Intent()
                    .setAction(Intent.ACTION_VIEW)
                    .setData("https://play.google.com/store/apps/details?id=com.google.android.apps.docs".toUri())

                context.startActivity(webIntent)
            }
        },
        modifier = Modifier
            .padding(30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo_drive_64dp),
                contentDescription = "Google Drive logo",
                modifier = Modifier
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
                    .size(32.dp)
            )
            Text(
                text = stringResource(R.string.storage_opengoogledrive)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageHome(navActions: NavActions, gaVM: GAVAPIViewModel) {
    val accountState = gaVM.accountInfo.collectAsState()
    val context = LocalContext.current

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
                    IconButton(
                        onClick = {
                            navActions.debugAPI()
                        }
                    ) {}
                    IconButton(
                        onClick = {
                            navActions.navigateToAccountSettings()
                        }
                    ) {
                        AsyncImage(
                            model = accountState.value.accountAvatarURI ?: R.drawable.account_circle_24px,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            )
        }, bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navActions.navigateToSeminarsHome()
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.business_center_24px),
                            contentDescription = stringResource(R.string.accessibility_seminarsMenu)
                        )
                    },
                    label = { Text(stringResource(R.string.home_seminars)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navActions.navigateToIOCHome()
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.docs_24px),
                            contentDescription = stringResource(R.string.accessibility_IOCmenu)
                        )
                    },
                    label = { Text(stringResource(R.string.home_ioc)) }
                )
                if (accountState.value.isProgrammer) {
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navActions.navigateToProgrammingHome()
                        },
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.language_24px),
                                contentDescription = stringResource(R.string.accessibility_programmingMenu)
                            )
                        },
                        label = { Text(stringResource(R.string.home_programming)) }
                    )
                }
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navActions.navigateToStorageHome()
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.folder_24px),
                            contentDescription = stringResource(R.string.accessibility_storageMenu)
                        )
                    },
                    label = { Text(stringResource(R.string.home_storage)) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "o(TヘTo)",
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.storage_nothinghereyet),
                textAlign = TextAlign.Center,
                fontSize = 32.sp
            )
            Text(
                text = stringResource(R.string.storage_checkbacklater),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            OpenGoogleDriveButton(context)
        }
    }
}