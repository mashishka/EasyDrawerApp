package com.example.easydrawer.ui.brush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BrushPreview(
    brushSize: Float,
    color: Color,
    opacity: Float
) {

    Canvas(
        modifier = Modifier
            .size(40.dp)
    ) {

        drawCircle(

            color = color.copy(
                alpha = opacity
            ),

            radius = brushSize,

            center = Offset(
                this.size.width / 2,
                this.size.height / 2
            )

        )

    }

}