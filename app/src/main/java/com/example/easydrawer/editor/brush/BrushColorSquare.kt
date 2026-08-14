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

@Composable
fun BrushColorSquare(
    brushColor: BrushColor,
    onColorChanged: (BrushColor) -> Unit
) {

    Canvas(
        modifier = Modifier
            .size(150.dp)
            .pointerInput(brushColor.hue) {

                detectDragGestures { change, _ ->

                    val x =
                        change.position.x
                            .coerceIn(
                                0f,
                                size.width.toFloat()
                            )

                    val y =
                        change.position.y
                            .coerceIn(
                                0f,
                                size.height.toFloat()
                            )

                    val saturation =
                        x / size.width

                    val brightness =
                        1f -
                                y / size.height

                    onColorChanged(

                        brushColor.copy(

                            saturation =
                                saturation,

                            brightness =
                                brightness

                        )

                    )

                }

            }

    ) {

        /*
         * BASE COLOR
         *
         * Только Hue.
         */
        val hueColor =
            Color.hsv(
                hue = brushColor.hue,
                saturation = 1f,
                value = 1f
            )


        /*
         * HUE -> WHITE
         *
         * saturation
         */
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.White,
                    hueColor
                )
            )
        )


        /*
         * TRANSPARENT -> BLACK
         *
         * brightness
         */
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black
                )
            )
        )


        /*
         * CURRENT POSITION
         */

        val x =
            brushColor.saturation *
                    size.width

        val y =
            (1f - brushColor.brightness) *
                    size.height

        drawCircle(
            color = Color.White,
            center = Offset(
                x,
                y
            ),
            radius = 7f
        )

        drawCircle(
            color = Color.Black,
            center = Offset(
                x,
                y
            ),
            radius = 7f,
            style = Stroke(
                width = 2f
            )
        )

    }
}