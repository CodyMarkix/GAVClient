package com.markix.gavclient.ui.apps.programming

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.markix.gavclient.NavActions
import com.markix.gavclient.R
import com.markix.gavclient.logic.data.ProgrammingAssignmentData
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.programming.PGSchoolYearViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun ProgrammingAssignment(assignmentData: ProgrammingAssignmentData, grade: Int) {
    var clicked by remember { mutableStateOf(0) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.tertiary,
        ),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        onClick = {
            clicked = 1
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = assignmentData.title ?: "[AssignmentName]",
                modifier = Modifier
                    .padding(5.dp)
            )
            if (assignmentData.completed != null) {
                Icon(
                    painter = painterResource(R.drawable.check_24px),
                    contentDescription = "Completed assignment icon",
                )
            }
        }
        val dueDate = assignmentData.validto?.toLocalDateTime(TimeZone.currentSystemDefault()) ?: Instant.parse("1970-01-01T00:00:00Z").toLocalDateTime(TimeZone.currentSystemDefault())
        Text(
            text = "${stringResource(R.string.programming_duedate)}: ${dueDate.day}. ${dueDate.month.ordinal + 1}. ${dueDate.year} ${dueDate.hour}:${if (dueDate.minute > 0) dueDate.minute else "00"}",
            modifier = Modifier
                .padding(5.dp)
        )
    }
    if (clicked == 1) {
        Dialog(
            onDismissRequest = {
                clicked = 0
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Text(
                        text = assignmentData.title ?: "[AssignmentName]",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    )
                    Text(
                        text = "100%: ${assignmentData.maximum}b (Bonus ${assignmentData.bonus}b)\n" +
                                "0%: ${assignmentData.minimum}b"
                    )
                    Row(
                        modifier = Modifier
                            .padding(0.dp)
                    ) {
                        Text("${stringResource(R.string.programming_pointsrecieved)}: ${assignmentData.result.toString()}b")
                        Text(
                            text = " (${grade})",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                    ) {
                        val dueDate = assignmentData.validto?.toLocalDateTime(TimeZone.currentSystemDefault()) ?: Instant.parse("1970-01-01T00:00:00Z").toLocalDateTime(TimeZone.currentSystemDefault())
                        Text(
                            text = "${stringResource(R.string.programming_duedate)}: ${dueDate.day}. ${dueDate.month.ordinal + 1}. ${dueDate.year} ${dueDate.hour}:${if (dueDate.minute > 0) dueDate.minute else "00"}"
                        )
                    }
                    TextButton(
                        onClick = {
                            clicked = 0
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgrammingSchoolYearScreen(gaVM: GAVAPIViewModel, navActions: NavActions, schoolYear: List<String>, pgsysVM: PGSchoolYearViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        pgsysVM.fetchAssignments(gaVM, schoolYear)
    }

    val accountState = gaVM.accountInfo.collectAsState()
    val pgSchoolYearScreenState = pgsysVM.uiState.collectAsState()

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
                            contentDescription = "Seminars icon"
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
                            contentDescription = "I.O.C. icon"
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
                            contentDescription = "Programming icon"
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
                            contentDescription = "Storage icon"
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
            verticalArrangement = Arrangement.Center
        ) {
            if (pgSchoolYearScreenState.value.assignments.size == 2) {
                Text(
                    text = "${stringResource(R.string.programming_semesterone)} (${schoolYear[0]})",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp)
                )
                if (pgSchoolYearScreenState.value.assignments[0].count() > 0) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 250.dp)
                    ) {
                        items(pgSchoolYearScreenState.value.assignments[0]) { index ->
                            ProgrammingAssignment(
                                index,
                                pgsysVM.convertPointsToGrade(
                                    index.result ?: 0,
                                    index.maximum?.toDouble() ?: 0.toDouble(),
                                    index.minimum?.toDouble() ?: 0.toDouble()
                                )
                            )
                        }
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "${stringResource(R.string.programming_semestertwo)} (${schoolYear[1]})",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp)
                )
                if (pgSchoolYearScreenState.value.assignments[1].count() > 0) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 300.dp)
                            .padding(0.dp, 25.dp)
                    ) {
                        items(pgSchoolYearScreenState.value.assignments[1]) { index ->
                            ProgrammingAssignment(
                                index,
                                pgsysVM.convertPointsToGrade(
                                    index.result ?: 0,
                                    index.maximum?.toDouble() ?: 0.toDouble(),
                                    index.minimum?.toDouble() ?: 0.toDouble()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
