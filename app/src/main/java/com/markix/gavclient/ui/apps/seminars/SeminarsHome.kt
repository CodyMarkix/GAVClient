package com.markix.gavclient.ui.apps.seminars

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.data.seminar.SeminarData
import com.markix.gavclient.logic.data.seminar.SeminarSlot
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.seminars.SeminarsHomeViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun Seminar(seminarData: SeminarData, lectorsPatched: List<String>, slot: SeminarSlot) {
    var showing by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp)
            .clickable {
                showing = true
            }
    ) {
        Text(
            text = seminarData.title ?: stringResource(R.string.seminars_notitle),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(10.dp, 10.dp, 0.dp, 5.dp)
        )
        LazyColumn(
            modifier = Modifier
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .height(45.dp)
        ) {
            items(lectorsPatched) { i ->
                Text(
                    text = i,
                )
            }
        }
        Text(
            text = seminarData.description ?: stringResource(R.string.seminars_nodescription),
            modifier = Modifier
                .padding(10.dp, 0.dp, 0.dp, 0.dp)
        )
    }

    if (showing) {
        AlertDialog(
            icon = {},
            title = {
                Text(text = seminarData.title ?: stringResource(R.string.seminars_notitle))
            },
            text = {
                Column() {
                    LazyColumn(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 5.dp)
                    ) {
                        items(lectorsPatched) { i ->
                            Text(i)
                        }
                    }
                    Text(
                        text = "Slot: ${slot.title}",
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 5.dp))

                    Text(
                        text = seminarData.description?.replace("\r\n", "") ?: stringResource(R.string.seminars_nodescription),
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    )
                    if (seminarData.registry != null) {
                        val registeredTimestamp = seminarData.registry!!.since!!.toLocalDateTime(TimeZone.currentSystemDefault())
                        Text(
                            text = "${stringResource(R.string.seminars_registeredsince)}: ${registeredTimestamp.day}. ${registeredTimestamp.month.ordinal + 1}. ${registeredTimestamp.year}  ${registeredTimestamp.hour}:${registeredTimestamp.minute}:${registeredTimestamp.second}"
                        )
                    }
                }
            },
            onDismissRequest = {
                showing = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showing = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {}
        )
    }
}

@Composable
fun AvailableSeminars(availableSeminars: MutableList<List<SeminarData>>, slots: List<SeminarSlot>, viewModel: SeminarsHomeViewModel) {
    if (availableSeminars.count() > 0) {
        LazyColumn() {
            items(slots.count()) { i ->
                Text(
                    text = slots[i].title ?: stringResource(R.string.seminars_notitle),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(10.dp, 0.dp, 10.dp, 15.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .height(240.dp)
                        .padding(0.dp, 0.dp, 0.dp, 20.dp)
                ) {
                    items(availableSeminars[i]) { j ->
                        Seminar(j, viewModel.convertLectorsStringToList(j.lectors ?: ""), slots[i])
                    }
                }
            }
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier
                .width(32.dp)
                .padding(50.dp, 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MySeminars(mySeminars: List<SeminarData>, vm: SeminarsHomeViewModel) {
    if (mySeminars.count() > 0) {
        LazyColumn() {
            items(mySeminars) { i ->
                Seminar(
                    i,
                    vm.convertLectorsStringToList(i.lectors ?: stringResource(R.string.seminars_anonymousnames)),
                    vm.findSlotById(i.slot ?: 0)
                )
            }
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier
                .width(32.dp)
                .padding(50.dp, 20.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeminarsHome(gaVM: GAVAPIViewModel, navActions: NavActions, viewModel: SeminarsHomeViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchSlots(gaVM)
        viewModel.getUserSeminars(gaVM)
        viewModel.getAvailableSeminars(gaVM)
    }

    val accountState = gaVM.accountInfo.collectAsState()
    val uiState = viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var offset by remember { mutableFloatStateOf(0f) }

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
        }
    ) { innerPadding ->
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    selectedTabIndex = 0
                },
                text = {
                    Text(stringResource(R.string.seminars_myseminars))
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    selectedTabIndex = 1
                },
                text = {
                    Text(stringResource(R.string.seminars_availableseminars))
                }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(PaddingValues(
                    innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    innerPadding.calculateTopPadding() + 70.dp,
                    innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    innerPadding.calculateBottomPadding()))
                .fillMaxWidth()
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offset += delta
                    }
                )
        ) {
            if (selectedTabIndex == 0) {
                MySeminars(uiState.value.mySeminars, viewModel)
            } else if (selectedTabIndex == 1) {
                AvailableSeminars(uiState.value.availableSeminars, uiState.value.slots, viewModel)
            }
        }
    }
}
