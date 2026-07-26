package com.example.counterapp.view.counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.counterapp.ui.theme.CounterAppTheme
import com.example.counterapp.view.counter.component.CounterButton
import com.example.counterapp.view.counter.component.ResetButton
import com.example.counterapp.view.counter.component.ResetConfirmationDialog
import com.example.counterapp.viewmodel.CounterViewModel

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = hiltViewModel()
) {
    val counter by viewModel.counter.collectAsStateWithLifecycle()
    var showResetConfirmation by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // カウンターの表示
            Text(
                text = counter.value.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )

            CounterButton(
                onClickIncrement = { viewModel.increment() },
                onClickDecrement = { viewModel.decrement() }
            )

            if (counter.value != 0) {
                ResetButton(
                    onClick = { showResetConfirmation = true }
                )
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }

    // リセット確認ダイアログ
    if (showResetConfirmation) {
        ResetConfirmationDialog(
            onConfirm = {
                viewModel.reset()
                showResetConfirmation = false
            },
            onDismiss = { showResetConfirmation = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CounterAppTheme {
        CounterScreen()
    }
}
