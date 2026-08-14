package com.example.easydrawer.ui.panels

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

object PanelRegistry {

    @Composable
    fun Render(
        id: String
    ) {

        when (id) {

            "brush" ->
                Text("Brush")

            "layers" ->
                Text("Layers")

            else ->
                Text(id)

        }

    }

}