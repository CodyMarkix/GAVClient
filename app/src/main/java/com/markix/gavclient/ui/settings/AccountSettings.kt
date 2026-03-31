package com.markix.gavclient.ui.settings

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColor
import coil3.compose.AsyncImage
import com.markix.gavclient.R
import com.markix.gavclient.logic.viewmodels.AccountData
import com.markix.gavclient.utils.addCharAtIndex
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.markix.gavclient.LocalSettingsRepository
import com.markix.gavclient.NavActions
import com.markix.gavclient.logic.repos.SettingsRepository
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FossLibraryEntry(name: String, link: String) {
    Text(buildAnnotatedString {
        append("\u2022 ")
        withLink(
            LinkAnnotation.Url(
                link,
                TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary))
            )
        ) {
            append(name)
        }
    })
}

@Composable
fun ThemeColor(coroutineScope: CoroutineScope, settingsRepository: SettingsRepository, currentColor: Long?, colorLong: Long) {
    val setColor: Color = when {
        colorLong.toInt() == 0x44464C54 -> {
            Color(0xFF825513)
        }
        else -> Color(colorLong)
    }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(48.dp)
            .clip(CircleShape)
            .background(setColor)
            .clickable {
                coroutineScope.launch {
                    settingsRepository.saveSeedColor(colorLong)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (currentColor == colorLong) {
            Image(
                painter = painterResource(R.drawable.check_24px),
                contentDescription = null,
                alignment = Alignment.Center
            )
        }
    }
}

@Composable
fun ThemeCustomColor(coroutineScope: CoroutineScope, onClick: () -> Unit) {
    val rainbowBrush = Brush.linearGradient(
        listOf(
            Color.Yellow,
            Color.Red,
            Color.Magenta,
            Color.Blue,
            Color.Green
        )
    )

    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(48.dp)
            .clip(CircleShape)
            .background(rainbowBrush)
            .clickable {
                coroutineScope.launch {
                    onClick()
                }
            }
    )
}

@Composable
fun ThemeColorPicker(controller: ColorPickerController) {
    val coroutineScope = rememberCoroutineScope()
    val settingsRepository = LocalSettingsRepository.current
    val currentColor = settingsRepository.seedColor.collectAsState(0xFF000000)
    var showColorPicker by remember { mutableStateOf(false) }
    var customColor by remember { mutableLongStateOf(0xFF000000) }
    val colors: List<Long> = listOf(
        0x44464C54, // reserved color, loads the stock theme
        0xFF34D1BF,
        0xFF28502E,
        0xFF1B2432,
        0xFF521945,
        0xFFFF715B,
        0xFF5F00BA,
        0xFFEE964B,
        0xFF388659
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
    ) {
        Text(
            text = "Theme color",
            fontSize = 18.sp,
            modifier = Modifier
                .padding(10.dp, 0.dp, 0.dp, 0.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.Center
        ) {
            colors.forEach { c ->
                ThemeColor(coroutineScope, settingsRepository, currentColor.value, c)
            }
            ThemeCustomColor(coroutineScope, onClick = { showColorPicker = true })
        }
    }
    if (showColorPicker) {
        Dialog(onDismissRequest = { }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(390.dp)
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(20.dp, 0.dp),
                        controller = controller,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            if (colorEnvelope.fromUser) {
                                coroutineScope.launch {
                                    settingsRepository.saveSeedColor(colorEnvelope.color.toColorLong())
                                    Log.d("com.markix.gavclient", "${colorEnvelope.color.toColorLong()}")
                                }
                            }
                        }
                    )

                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                Log.d("com.markix.gavclient", "Setting color: ${customColor.toColor()}")
                                settingsRepository.saveSeedColor(customColor)
                                showColorPicker = false
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            }

        }
    }
}

@Composable
fun AccountDetails(account: AccountData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(25.dp, 25.dp, 25.dp, 0.dp)
        ) {
            AsyncImage(
                model = account.accountAvatarURI
                    ?: painterResource(id = R.drawable.account_circle_24px),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .padding(25.dp, 20.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = (account.accountFirstName + " " + account.accountLastName)
                        ?: "John Android",
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = account.accountClass?.addCharAtIndex('.', 1) ?: "null",
                    fontSize = 16.sp
                )
                Text(
                    text = account.accountMail ?: "name.surname@gyarab.cz",
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettings(navActions: NavActions, gaViewModel: GAVAPIViewModel) {
    val context = LocalContext.current
    val accountState = gaViewModel.accountInfo.collectAsState()
    var showOpenSourceModal by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val controller = rememberColorPickerController()

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

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            AccountDetails(accountState.value)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(0.dp, 60.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    onClick = {
                        val intent = Intent(Settings.ACTION_SETTINGS)
                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.accountsettings_opengooglesettings),
                        fontSize = 18.sp
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 0.dp, 0.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    onClick = {
                        navActions.navigateToLoginScreen()
                        gaViewModel.signOut()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.accountsettings_signout),
                        fontSize = 18.sp
                    )
                }
            }
            ThemeColorPicker(controller)
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = stringResource(R.string.foss_info),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(5.dp, 20.dp)
                    .clickable() {
                        showOpenSourceModal = 1
                    }
            )

        }
        if (showOpenSourceModal == 1) {
            AlertDialog(
                icon = {},
                title = {
                    Text("Open-source")
                },
                text = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = stringResource(R.string.foss_builtwithfoss),
                            modifier = Modifier
                                .padding(0.dp, 10.dp)
                        )

                        FossLibraryEntry("square/okhttp", "https://github.com/square/okhttp")
                        FossLibraryEntry("jordond/materialkolor", "https://github.com/jordond/MaterialKolor")
                        FossLibraryEntry("skydoves/colorpicker-compose", "https://github.com/skydoves/colorpicker-compose")
                        FossLibraryEntry("hub4j/github-api", "https://github.com/hub4j/github-api")
                        FossLibraryEntry("Jetpack (androidx/androidx)", "https://github.com/androidx/androidx/")
                    }
                },
                onDismissRequest = {
                    showOpenSourceModal = 0
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showOpenSourceModal = 0
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {}
            )
        }
    }
}