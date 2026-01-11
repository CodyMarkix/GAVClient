package com.markix.gavclient.ui.apps.programming

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.markix.gavclient.NavDestinations
import com.markix.gavclient.R
import com.markix.gavclient.ui.theme.AppTheme

@Composable
fun ProgrammingSchoolYear(year: String) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp, 10.dp, 40.dp)
    ) {
        Text(
            text = year,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable {
                    expanded = true
                }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("1. pololetí") },
                onClick = {
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("2. pololetí") },
                onClick = {
                    expanded = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgrammingHome(navigationController: NavController) {
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
                            navigationController.navigate(NavDestinations.ACCOUNT_SETTINGS)
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.account_circle_24px),
                            contentDescription = null
                        )
                    }
                    IconButton(
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
        }, bottomBar = {
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
                        navigationController.navigate(NavDestinations.IOC_HOME)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(0.dp, 30.dp, 0.dp, 0.dp)
        ) {
            item {
                
            }
        }
    }
}

@Preview
@Composable
fun ProgrammingHomePreview() {
    AppTheme() {
        ProgrammingHome(NavController(LocalContext.current))
    }
}