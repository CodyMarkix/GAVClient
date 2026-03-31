package com.markix.gavclient.ui.apps.ioc

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextButton
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
import com.markix.gavclient.logic.data.ioc.IOCAgendaData
import com.markix.gavclient.logic.data.ioc.IOCTopic
import com.markix.gavclient.logic.data.ioc.IOCTopicData
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.ioc.IOCAgendaViewModel

@Composable
fun IOCTopicEntry(data: IOCTopic) {
    var clicked by remember { mutableStateOf(false) }

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
                clicked = true
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

    if (clicked) {
        AlertDialog(
            icon = {},
            title = {
                Text(data.title ?: stringResource(R.string.ioc_noname))
            },
            text = {
                Text(
                    text = data.description ?: stringResource(R.string.ioc_nodescription),
                    modifier = Modifier
                        .heightIn(0.dp, 235.dp)
                        .verticalScroll(rememberScrollState())
                )
            },
            onDismissRequest = {
                clicked = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        clicked = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOCAgenda(name: String, id: Int, navActions: NavActions, gaVM: GAVAPIViewModel, agendaVM: IOCAgendaViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        agendaVM.getTopicList(gaVM, id ?: 0)
        agendaVM.getAgendaKeywords(gaVM, id ?: 0)
    }

    val accountState = gaVM.accountInfo.collectAsState()
    val uiState = agendaVM.uiState.collectAsState()
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
                text = name ?: "{Title}",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                lineHeight = 1.25.em,
                modifier = Modifier
                    .padding(0.dp, 25.dp, 0.dp, 30.dp)
            )
            if (uiState.value.topics.count() > 0) {
                LazyColumn {
                    items (uiState.value.topics) { topic ->
                        if (uiState.value.filter.isNotEmpty()) {
                            if (topic.keywords!!.intersect(uiState.value.filter.toSet()).isNotEmpty()) {
                                AnimatedVisibility(
                                    visible = true,
                                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                                    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
                                ) {
                                    IOCTopicEntry(topic)
                                }
                            }
                        } else {
                            IOCTopicEntry(topic)
                        }
                    }
                }
                if (uiState.value.loadingTopics) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(10.dp)
                            .width(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
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
                                    if (keyword.id in uiState.value.filter) {
                                        agendaVM.removeKeywordFilter(keyword.id ?: 0)
                                    } else {
                                        agendaVM.selectKeywordFilter(keyword.id ?: 0)
                                    }
                                    Log.d("com.markix.gavclient", "Selected keywords: ${uiState.value.filter}")
                                },
                                label = {
                                    Text(keyword.keyword ?: "null")
                                },
                                selected = keyword.id in uiState.value.filter,
                                leadingIcon = if (keyword.id in uiState.value.filter) {
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