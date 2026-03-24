package com.markix.gavclient.ui.apps.ioc

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.data.IOCTopicData
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.ioc.IOCAgendaViewModel

@Composable
fun IOCTopicEntry(data: IOCTopicData) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp, 20.dp, 20.dp)
            .clickable {

            }
    ) {
        Column(
        ) {
            Text(
                text = data.title ?: stringResource(R.string.ioc_noname),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(30.dp, 20.dp, 0.dp, 10.dp)
            )
            Text(
                text = data.description ?: stringResource(R.string.ioc_nodescription),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(30.dp, 0.dp, 20.dp, 30.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOCAgenda(id: Int, name: String, navActions: NavActions, GAViewModel: GAVAPIViewModel, AgendaModel: IOCAgendaViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        AgendaModel.getTopicList(GAViewModel, id)
        AgendaModel.getAgendaKeywords(GAViewModel, id)
        AgendaModel.refreshTopicList()
    }

    val accountState = GAViewModel.accountInfo.collectAsState()
    val uiState = AgendaModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

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
                        navActions.navigateToSeminarsHome()
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
                        navActions.navigateToIOCHome()
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
                    onClick = {
                        navActions.navigateToStorageHome()
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.folder_24px),
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(R.string.home_storage)) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Image(
                    painter = painterResource(id = R.drawable.filter_alt_24px),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
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
                lineHeight = 1.25.em,
                modifier = Modifier
                    .padding(0.dp, 25.dp, 0.dp, 30.dp)
            )
            if (uiState.value.topics.count() > 0) {
                LazyColumn {
                    items (uiState.value.selectedTopics.size) { index ->
                        IOCTopicEntry(IOCTopicData(
                            id = uiState.value.topics[index].id,
                            title = uiState.value.topics[index].title,
                            description = uiState.value.topics[index].description,
                        ))
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.width(32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ioc_tags),
                        fontSize = 24.sp
                    )
                    FlowRow() {
                        uiState.value.keywords.forEach { keyword ->
                            FilterChip(
                                onClick = {
                                    AgendaModel.refreshTopicList()
                                    if (keyword.id in uiState.value.selectedFilters) {
                                        uiState.value.selectedFilters -= (keyword.id ?: 0)
                                    } else {
                                        uiState.value.selectedFilters += (keyword.id ?: 0)
                                    }
                                },
                                label = {
                                    Text(keyword.keyword ?: "null")
                                },
                                selected = keyword.id in uiState.value.selectedFilters,
                                leadingIcon = if (keyword.id in uiState.value.selectedFilters) {
                                    {
                                        Icon(
                                            painter = painterResource(R.drawable.check_24px),
                                            contentDescription = null,
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else  {
                                    null
                                },
                                modifier = Modifier
                                    .padding(2.5.dp, 0.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}