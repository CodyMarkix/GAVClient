package com.markix.gavclient.apps.ioc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.markix.gavclient.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOCHome(navigationController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xffdcb486),
                    titleContentColor = Color(0xffffffff),
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
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}