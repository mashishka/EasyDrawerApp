package com.example.easydrawer.ui.brush.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CurrentColorPreview(
    color: Color
) {

    Canvas(
        modifier = Modifier
            .size(50.dp)
    ) {

        drawCircle(
            color = Color.LightGray
        )

        drawCircle(
            color = color
        )

    }

}