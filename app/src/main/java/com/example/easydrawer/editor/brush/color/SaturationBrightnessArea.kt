package com.example.easydrawer.ui.brush.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun SaturationBrightnessArea(
    hue: Float,
    saturation: Float,
    brightness: Float,
    onColorChanged: (Float, Float) -> Unit
) {

    Canvas(

        modifier = Modifier
            .size(200.dp)
            .pointerInput(hue) {

                detectDragGestures(

                    onDragStart = { offset ->

                        updateColor(
                            offset = offset,
                            width = size.width.toFloat(),
                            height = size.height.toFloat(),
                            onColorChanged = onColorChanged
                        )

                    },

                    onDrag = { change, _ ->

                        updateColor(
                            offset = change.position,
                            width = size.width.toFloat(),
                            height = size.height.toFloat(),
                            onColorChanged = onColorChanged
                        )

                    }

                )

            }

    ) {

        // Основной цвет текущего Hue

        val hueColor =
            Color.hsv(
                hue = hue,
                saturation = 1f,
                value = 1f
            )

        // Сначала полностью насыщенный цвет

        drawRect(
            color = hueColor
        )

        // Затем белый -> прозрачный.
        // Это создаёт Saturation.

        drawRect(

            brush =
                Brush.horizontalGradient(

                    colors = listOf(
                        Color.White,
                        Color.Transparent
                    )

                )

        )

        // Затем прозрачный -> чёрный.
        // Это создаёт Brightness.

        drawRect(

            brush =
                Brush.verticalGradient(

                    colors = listOf(
                        Color.Transparent,
                        Color.Black
                    )

                )

        )

        // Маркер выбранного цвета

        val x =
            saturation * size.width

        val y =
            (1f - brightness) * size.height

        drawCircle(

            color = Color.White,

            radius = 8f,

            center = Offset(
                x,
                y
            )

        )

        drawCircle(

            color = Color.Black,

            radius = 6f,

            center = Offset(
                x,
                y
            )

        )

    }

}

private fun updateColor(
    offset: Offset,
    width: Float,
    height: Float,
    onColorChanged: (Float, Float) -> Unit
) {

    val saturation =
        (offset.x / width)
            .coerceIn(0f, 1f)

    val brightness =
        (1f - offset.y / height)
            .coerceIn(0f, 1f)

    onColorChanged(
        saturation,
        brightness
    )

}