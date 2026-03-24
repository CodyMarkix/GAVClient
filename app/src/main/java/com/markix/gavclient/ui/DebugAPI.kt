package com.markix.gavclient.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.markix.gavclient.logic.viewmodels.DebugAPIViewModel
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.launch

@Composable
fun DebugAPI(gaVM: GAVAPIViewModel, viewModel: DebugAPIViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val apiOutput = viewModel.uiState.collectAsState()
    val textFieldState = rememberTextFieldState()
    val scroll = rememberScrollState(0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                state = textFieldState,
                label = { Text("Enter request") }
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.callAPI(textFieldState.text.toString(), gaVM)
                    }
                },
                modifier = Modifier
                    .padding(10.dp, 0.dp)
            ) {
                Text("Send")
            }
        }

        LazyColumn() {
            item {
                Text(
                    text = apiOutput.value.apiOutput,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(5.dp)
                        .height(240.dp)
                        .verticalScroll(scroll),
                )
            }
        }
    }
}