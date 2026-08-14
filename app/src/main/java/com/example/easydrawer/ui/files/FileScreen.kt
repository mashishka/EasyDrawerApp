package com.example.easydrawer.ui.files

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileScreen(
    onNewDrawing: () -> Unit,
    onOpenDrawing: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "My Drawings",
            style = MaterialTheme.typography.headlineSmall
        )

        Button(
            onClick = onNewDrawing
        ) {
            Text("New drawing")
        }

        Button(
            onClick = onOpenDrawing
        ) {
            Text("Open drawing")
        }
    }
}