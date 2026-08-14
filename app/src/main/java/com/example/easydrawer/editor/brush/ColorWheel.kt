package com.example.easydrawer.ui.brush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.brush.BrushColor
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BrushColorWheel(
    brushColor: BrushColor,
    onColorChanged: (BrushColor) -> Unit
) {

    Canvas(
        modifier = Modifier
            .size(550.dp)
            .pointerInput(Unit) {

                detectDragGestures { change, _ ->

                    val center = Offset(
                        size.width / 2f,
                        size.height / 2f
                    )

                    val dx =
                        change.position.x - center.x

                    val dy =
                        change.position.y - center.y

                    var hue =
                        Math.toDegrees(
                            atan2(
                                dy.toDouble(),
                                dx.toDouble()
                            )
                        ).toFloat()

                    if (hue < 0f) {
                        hue += 360f
                    }

                    onColorChanged(
                        brushColor.copy(
                            hue = hue
                        )
                    )

                    change.consume()
                }
            }
    ) {

        val center = Offset(
            size.width / 2f,
            size.height / 2f
        )

        /*
         * Размер цветового кольца.
         */
        val radius =
            size.minDimension / 2f - 15f

        /*
         * Толщина кольца.
         *
         * Внутри него останется большое
         * свободное пространство для квадрата.
         */
        val ringWidth = 55f

        /*
         * HUE RING
         */
        drawCircle(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red
                ),
                center = center
            ),
            center = center,
            radius = radius,
            style = Stroke(
                width = ringWidth
            )
        )

        /*
         * POINTER
         */
        val radians =
            Math.toRadians(
                brushColor.hue.toDouble()
            )

        val indicator = Offset(
            x =
                center.x +
                        cos(radians).toFloat() *
                        radius,

            y =
                center.y +
                        sin(radians).toFloat() *
                        radius
        )

        drawCircle(
            color = Color.White,
            center = indicator,
            radius = 9f
        )

        drawCircle(
            color = Color.Black,
            center = indicator,
            radius = 9f,
            style = Stroke(
                width = 2f
            )
        )
    }
}