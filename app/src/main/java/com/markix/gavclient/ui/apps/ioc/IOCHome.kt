package com.markix.gavclient.ui.apps.ioc

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.data.ioc.IOCAgendaData
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.ioc.IOCHomeViewModel
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.markix.gavclient.logic.repos.SettingsRepository
import com.markix.gavclient.logic.viewmodels.GAVClientViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun IOCAgendaEntry(agenda: IOCAgendaData, navActions: NavActions, context: Context) {
    Card(
        colors = CardColors(
            containerColor = if (agenda.archived == 1) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (agenda.archived == 1) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            if (agenda.id != null) {
                navActions.navigateToIOCAgenda(agenda.title ?: "", agenda.id)
            } else {
                Toast.makeText(context, "Cannot open invalid agenda", Toast.LENGTH_SHORT).show()
            }
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
                    text = agenda.title ?: "Bez Jména",
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )
                if (agenda.archived == 1) {
                    Image(
                        painter = painterResource(R.drawable.business_center_24px),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp, 0.dp)
                    )
                }
            }
            Text(
                text = agenda.description ?: "",
                textAlign = TextAlign.Left
            )
        }
    }
}

@Composable
fun IOCAgendaList(agendas: List<IOCAgendaData>, navActions: NavActions, context: Context) {
    LazyColumn {
        items(agendas) { agenda ->
            IOCAgendaEntry(agenda, navActions, context)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun IOCHome(navActions: NavActions, gaViewModel: GAVAPIViewModel, gavclientVM: GAVClientViewModel, settingsRepository: SettingsRepository, iocHViewModel: IOCHomeViewModel = viewModel()) {
    var lastUpdateCheck = settingsRepository.lastUpdateCheck.collectAsState("1970-01-01T00:00:00Z")
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val snackbarTitle = stringResource(R.string.app_updatesavailable)
    val snackbarAction = stringResource(R.string.app_updateaction)

    LaunchedEffect(Unit) {
        iocHViewModel.getIOCAgendaList(gaViewModel)
        // if (Clock.System.now().epochSeconds - 604800 >= Instant.parse(lastUpdateCheck.value ?: "1970-01-01T00:00:00Z").epochSeconds) {
            if (gavclientVM.checkForUpdates()) {
                val result = snackbarHostState.showSnackbar(
                    message = snackbarTitle,
                    actionLabel = snackbarAction
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        val githubIntent = Intent()
                            .setAction(Intent.ACTION_VIEW)
                            .setData("https://github.com/CodyMarkix/GAVClient/releases/latest".toUri())
                        context.startActivity(githubIntent)
                    }
                    SnackbarResult.Dismissed -> {}
                }
            }
            // settingsRepository.saveLastUpdateCheck(Clock.System.now().toString())
        // }
    }

    val accountState = gaViewModel.accountInfo.collectAsState()
    val uiState = iocHViewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(R.string.ioc_agendalist),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(30.dp)
            )
            if (uiState.value.agendas.count() > 0) {
                IOCAgendaList(uiState.value.agendas, navActions, context)
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.width(32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}