package com.markix.gavclient.ui.apps.ioc

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import com.markix.gavclient.R
import com.markix.gavclient.logic.data.IOCTopicData
import com.markix.gavclient.ui.theme.AppTheme

@Composable
fun IOCAgendaEntry(data: IOCTopicData) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier
            .padding(20.dp, 0.dp, 20.dp, 20.dp)
            .clickable {

            }
    ) {
        Column(
        ) {
            Text(
                text = data.title ?: "Bez jména",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(30.dp, 20.dp, 0.dp, 10.dp)
            )
            Text(
                text = data.description ?: "Bez popisku",
                maxLines = 2,
                modifier = Modifier
                    .padding(30.dp, 0.dp, 20.dp, 30.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOCAgenda(name: String, id: Int, navigationController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.filter_alt_24px),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                AssistChip(
                    onClick = { Log.d("com.markix.gavclient", "clicked assist chip") },
                    label = { Text("Anglický jazyk") }
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()

        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(0.dp, 25.dp, 0.dp, 30.dp)
            )
            LazyColumn {
                items (5) { item ->
                    IOCAgendaEntry(IOCTopicData(
                        id = 599,
                        title = "Místa paměti (Vila Tugendhat)",
                        description = "Výběr místa/lokality dle vlastního zájmu (např. protektorátní Praha), vytvoření návrhu naučené stezky/vytvoření audiovizuálního průvodce.\r\nFormy: krátký film, ročníková práce, odborný poster\r\n",
                    ))
                }
            }
        }
    }
}

@Composable
@Preview
fun IOCAgendaPreview() {
    AppTheme() {
        IOCAgenda("Individuální odborní činnost 2025/26", 17, NavController(LocalContext.current))
    }
}