package com.example.easydrawer.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.easydrawer.ui.screens.EditorScreen

@Composable
fun EasyDrawerApp() {

    MaterialTheme {

        Surface {

            EditorScreen()

        }

    }

}