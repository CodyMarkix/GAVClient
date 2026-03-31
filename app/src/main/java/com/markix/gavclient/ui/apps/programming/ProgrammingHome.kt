package com.markix.gavclient.ui.apps.programming

import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.programming.PGHomeViewModel
import androidx.compose.ui.res.stringResource

@Composable
fun ProgrammingSchoolYear(year: List<String>, navActions: NavActions) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier
                .padding(10.dp),
            onClick = {
                navActions.navigateToProgrammingSchoolYear(year)
            }
        ) {
            Text(
                text = "${year[0]}/${year[1].slice(2..3)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(40.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgrammingHome(navActions: NavActions, gaViewModel: GAVAPIViewModel, pgHomeViewModel: PGHomeViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        pgHomeViewModel.getSchoolYears(gaViewModel)
    }

    val accountState = gaViewModel.accountInfo.collectAsState()
    val pgHomeState = pgHomeViewModel.uiState.collectAsState()
    for (a in pgHomeState.value.schoolYears) {
        Log.d("com.markix.gavclient", "${a[0]}, ${a[1]}")
    }

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
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (pgHomeState.value.schoolYears.count() > 0) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(pgHomeState.value.schoolYears) { schoolYear ->
                        ProgrammingSchoolYear(schoolYear, navActions)
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
    }
}
