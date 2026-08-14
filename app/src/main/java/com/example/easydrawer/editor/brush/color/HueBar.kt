package com.example.easydrawer.ui.brush.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun HueBar(
    hue: Float,
    onHueChanged: (Float) -> Unit
) {

    Canvas(

        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .pointerInput(Unit) {

                detectDragGestures(

                    onDragStart = { offset ->

                        updateHue(
                            offset.x,
                            size.width.toFloat(),
                            onHueChanged
                        )

                    },

                    onDrag = { change, _ ->

                        updateHue(
                            change.position.x,
                            size.width.toFloat(),
                            onHueChanged
                        )

                    }

                )

            }

    ) {

        drawRoundRect(

            brush =
                Brush.horizontalGradient(

                    colors = listOf(

                        Color.Red,
                        Color.Yellow,
                        Color.Green,
                        Color.Cyan,
                        Color.Blue,
                        Color.Magenta,
                        Color.Red

                    )

                ),

            cornerRadius =
                androidx.compose.ui.geometry.CornerRadius(
                    12f,
                    12f
                )

        )

        // Индикатор Hue

        val x =
            hue / 360f * size.width

        drawCircle(

            color = Color.White,

            radius = 9f,

            center =
                androidx.compose.ui.geometry.Offset(
                    x,
                    size.height / 2
                )

        )

        drawCircle(

            color = Color.Black,

            radius = 6f,

            center =
                androidx.compose.ui.geometry.Offset(
                    x,
                    size.height / 2
                )

        )

    }

}

private fun updateHue(
    x: Float,
    width: Float,
    onHueChanged: (Float) -> Unit
) {

    val hue =
        (x / width * 360f)
            .coerceIn(
                0f,
                360f
            )

    onHueChanged(hue)

}