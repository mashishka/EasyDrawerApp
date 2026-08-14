package com.example.easydrawer.ui.brush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.brush.BrushColor
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun BrushColorPicker(
    brushColor: BrushColor,
    onColorChanged: (BrushColor) -> Unit
) {
    val currentBrushColor =
        rememberUpdatedState(brushColor)

    val currentOnColorChanged =
        rememberUpdatedState(onColorChanged)

    Canvas(
        modifier = Modifier
            .size(320.dp)
            .pointerInput(Unit) {

                var dragMode: DragMode? = null

                detectDragGestures(

                    onDragStart = { position ->

                        val center = Offset(
                            size.width / 2f,
                            size.height / 2f
                        )

                        val dx =
                            position.x - center.x

                        val dy =
                            position.y - center.y

                        val distance =
                            hypot(dx, dy)

                        val outerRadius =
                            minOf(
                                size.width,
                                size.height
                            ) / 2f - 4f

                        val ringWidth = 26f

                        val innerRadius =
                            outerRadius - ringWidth

                        val squareSize =
                            innerRadius * 1.55f

                        val left =
                            center.x -
                                    squareSize / 2f

                        val top =
                            center.y -
                                    squareSize / 2f

                        val right =
                            left + squareSize

                        val bottom =
                            top + squareSize

                        dragMode =
                            when {

                                distance >= innerRadius - 12f &&
                                        distance <= outerRadius + 12f ->
                                    DragMode.HUE

                                position.x in left..right &&
                                        position.y in top..bottom ->
                                    DragMode.SATURATION_BRIGHTNESS

                                else ->
                                    null
                            }
                    },

                    onDrag = { change, _ ->

                        val mode = dragMode
                            ?: return@detectDragGestures

                        val center = Offset(
                            size.width / 2f,
                            size.height / 2f
                        )

                        val outerRadius =
                            minOf(
                                size.width,
                                size.height
                            ) / 2f - 4f

                        val ringWidth = 26f

                        val innerRadius =
                            outerRadius - ringWidth

                        val squareSize =
                            innerRadius * 1.55f

                        val left =
                            center.x -
                                    squareSize / 2f

                        val top =
                            center.y -
                                    squareSize / 2f

                        val color =
                            currentBrushColor.value

                        when (mode) {

                            DragMode.HUE -> {

                                val dx =
                                    change.position.x -
                                            center.x

                                val dy =
                                    change.position.y -
                                            center.y

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

                                currentOnColorChanged.value(
                                    color.copy(
                                        hue = hue
                                    )
                                )
                            }

                            DragMode.SATURATION_BRIGHTNESS -> {

                                val saturation =
                                    (
                                            (
                                                    change.position.x -
                                                            left
                                                    ) / squareSize
                                            ).coerceIn(
                                            0f,
                                            1f
                                        )

                                val brightness =
                                    1f -
                                            (
                                                    (
                                                            change.position.y -
                                                                    top
                                                            ) / squareSize
                                                    ).coerceIn(
                                                    0f,
                                                    1f
                                                )

                                currentOnColorChanged.value(
                                    color.copy(
                                        saturation = saturation,
                                        brightness = brightness
                                    )
                                )
                            }
                        }

                        change.consume()
                    },

                    onDragEnd = {
                        dragMode = null
                    },

                    onDragCancel = {
                        dragMode = null
                    }
                )
            }
    ) {

        val center = Offset(
            size.width / 2f,
            size.height / 2f
        )

        val outerRadius =
            minOf(
                size.width,
                size.height
            ) / 2f - 4f

        val ringWidth = 26f

        val innerRadius =
            outerRadius - ringWidth

        /*
         * -------------------------
         * HUE RING
         * -------------------------
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
            radius = outerRadius,
            style = Stroke(
                width = ringWidth
            )
        )

        /*
         * -------------------------
         * SATURATION / BRIGHTNESS
         * -------------------------
         */

        val squareSize =
            innerRadius * 1.55f

        val left =
            center.x -
                    squareSize / 2f

        val top =
            center.y -
                    squareSize / 2f

        val hueColor =
            Color.hsv(
                hue = brushColor.hue,
                saturation = 1f,
                value = 1f
            )

        // White -> Hue
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.White,
                    hueColor
                )
            ),
            topLeft = Offset(
                left,
                top
            ),
            size = Size(
                squareSize,
                squareSize
            )
        )

        // Transparent -> Black
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black
                )
            ),
            topLeft = Offset(
                left,
                top
            ),
            size = Size(
                squareSize,
                squareSize
            )
        )

        /*
         * -------------------------
         * HUE POINTER
         * -------------------------
         */

        val hueRadians =
            Math.toRadians(
                brushColor.hue.toDouble()
            )

        val huePointer =
            Offset(
                x =
                    center.x +
                            cos(hueRadians).toFloat() *
                            outerRadius,

                y =
                    center.y +
                            sin(hueRadians).toFloat() *
                            outerRadius
            )

        drawCircle(
            color = Color.White,
            center = huePointer,
            radius = 8f
        )

        drawCircle(
            color = Color.Black,
            center = huePointer,
            radius = 8f,
            style = Stroke(
                width = 2f
            )
        )

        /*
         * -------------------------
         * SATURATION / BRIGHTNESS POINTER
         * -------------------------
         */

        val saturationX =
            left +
                    brushColor.saturation *
                    squareSize

        val brightnessY =
            top +
                    (1f - brushColor.brightness) *
                    squareSize

        val sbPointer =
            Offset(
                saturationX,
                brightnessY
            )

        drawCircle(
            color = Color.White,
            center = sbPointer,
            radius = 8f
        )

        drawCircle(
            color = Color.Black,
            center = sbPointer,
            radius = 8f,
            style = Stroke(
                width = 2f
            )
        )
    }
}

private enum class DragMode {
    HUE,
    SATURATION_BRIGHTNESS
}