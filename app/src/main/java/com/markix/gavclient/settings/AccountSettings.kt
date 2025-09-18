package com.markix.gavclient.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markix.gavclient.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettings() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xffdcb486),
                    titleContentColor = Color(0xffffffff),
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
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(25.dp, 25.dp, 25.dp, 0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.account_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(25.dp, 20.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        text = "John Mantle",
                        fontSize = 24.sp
                    )
                    Text(
                        text = "john.mantle@gyarab.cz",
                        fontSize = 18.sp
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(25.dp, 60.dp, 0.dp, 0.dp),
                colors = ButtonColors(
                    containerColor = Color(0xffdcb486),
                    contentColor = Color(0xffffffff),
                    disabledContainerColor = Color(0xffdcb486),
                    disabledContentColor = Color(0xffffffff),
                ),
                onClick = {}
            ) {
                Text(
                    text = "Otevřít nastavení Google",
                    fontSize = 18.sp
                )
            }
            Button(
                modifier = Modifier
                    .padding(25.dp, 10.dp, 0.dp, 0.dp),
                colors = ButtonColors(
                    containerColor = Color(0xffdcb486),
                    contentColor = Color(0xffffffff),
                    disabledContainerColor = Color(0xffdcb486),
                    disabledContentColor = Color(0xffffffff),
                ),
                onClick = {}
            ) {
                Text(
                    text = "Odhlásit se",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun AccountSettingsPreview() {
    AccountSettings()
}