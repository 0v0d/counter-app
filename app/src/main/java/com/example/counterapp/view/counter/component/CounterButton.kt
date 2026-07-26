package com.example.counterapp.view.counter.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CounterButton(
    onClickIncrement: () -> Unit,
    onClickDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 減少ボタン
        FilledIconButton(
            onClick = onClickDecrement,
            modifier = Modifier.size(64.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),

            ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Decrement",
                modifier = Modifier.size(32.dp)
            )
        }

        // 増加ボタン
        FilledIconButton(
            onClick = onClickIncrement,
            modifier = Modifier.size(64.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Increment",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
