package com.example.easydrawer.ui.layers

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.easydrawer.editor.Layer


@Composable
fun LayerEditor(
    layer: Layer
) {

    Column {

        Text(
            "Name"
        )


        TextField(

            value = layer.name,

            onValueChange = {
                layer.name = it
            }

        )


        Text(
            "Opacity ${(layer.opacity * 100).toInt()}%"
        )


        Slider(

            value = layer.opacity,

            onValueChange = {
                layer.opacity = it
            },

            valueRange = 0f..1f

        )

    }

}