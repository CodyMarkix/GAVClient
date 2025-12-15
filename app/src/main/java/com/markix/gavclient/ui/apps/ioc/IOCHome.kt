package com.markix.gavclient.ui.apps.ioc

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.markix.gavclient.R
import com.markix.gavclient.logic.models.GAVAPIViewModel

@Composable
fun AgendaEntry(name: String) {
    Button(
        onClick = {

        },
        modifier = Modifier
            .padding(0.dp, 40.dp)
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Left,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOCHome(navigationController: NavController, viewModel: GAVAPIViewModel = viewModel()) {
    Log.d("com.markix.gavclient", viewModel.accountInfo.value.tokenId?.id ?: "null")
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text("Gyarab Výuka")
                },
                actions = {
                    IconButton(
                        onClick = {
                            navigationController.navigate("account_settings")
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.account_circle_24px),
                            contentDescription = null
                        )
                    }
                    IconButton (
                        onClick = {
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.more_vert_24px),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {

                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.business_center_24px),
                            contentDescription = null
                        )
                    },
                    label = { Text("Semináře") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        // navigationController.navigate(IOC)
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.docs_24px),
                            contentDescription = null
                        )
                    },
                    label = { Text("IOČ") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.language_24px),
                            contentDescription = null
                        )
                    },
                    label = { Text("Prográmko") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.folder_24px),
                            contentDescription = null
                        )
                    },
                    label = { Text("Úložiště") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Seznam Agend",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(30.dp)
            )
            LazyColumn() {
                /*
                * TODO: While most students won't study more than 4 years, it
                * should be accounted for if a student needs to repeat a year
                */

            }
        }
    }
}

@Composable
@Preview
fun IOCHomePreview() {
    IOCHome(NavController(LocalContext.current))
}