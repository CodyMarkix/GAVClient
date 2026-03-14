package com.markix.gavclient.ui.apps.ioc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.data.IOCAgendaData
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.ioc.IOCHomeViewModel
import androidx.compose.ui.res.stringResource

@Composable
fun IOCAgendaEntry(
    id: Int,
    name: String,
    description: String,
    status: Int,
    archived: Int,
    roles: List<Int>,
    navActions: NavActions
    ) {
    Card(
        colors = CardColors(
            containerColor = if (archived == 1) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (archived == 1) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            navActions.navigateToIOCAgenda(id, name)
        },
        modifier = Modifier
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = name,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )
                if (archived == 1) {
                    Image(
                        painter = painterResource(R.drawable.business_center_24px),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp, 0.dp)
                    )
                }
            }
            Text(
                text = description,
                textAlign = TextAlign.Left
            )
        }
    }
}

@Composable
fun IOCAgendaList(agendas: List<IOCAgendaData>, navActions: NavActions) {
    LazyColumn {
        items(agendas.size) { index ->
            IOCAgendaEntry(
                agendas[index].id ?: 1,
                agendas[index].title ?: "{Agenda Name}",
                agendas[index].description ?: "{Agenda Desc}",
                agendas[index].status ?: 0,
                agendas[index].archived ?: 0,
                agendas[index].roles ?: emptyList<Int>(),
                navActions
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOCHome(navActions: NavActions, gaViewModel: GAVAPIViewModel, iocHViewModel: IOCHomeViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        iocHViewModel.getIOCAgendaList(gaViewModel)
    }

    val accountState = gaViewModel.accountInfo.collectAsState()
    val uiState = iocHViewModel.uiState.collectAsState()

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
                    label = { Text(stringResource(R.string.home_seminars)) }
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
                    label = { Text(stringResource(R.string.home_ioc)) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navActions.navigateToProgrammingHome()
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.language_24px),
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(R.string.home_programming)) }
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
                    label = { Text(stringResource(R.string.home_storage)) }
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
                stringResource(R.string.ioc_agendalist),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(30.dp)
            )
            IOCAgendaList(uiState.value.agendas, navActions)
        }
    }
}