package com.example.easydrawer.ui.layers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.Layer
import com.example.easydrawer.ui.canvas.DrawStroke
import kotlin.math.max


@Composable
fun LayerPreview(
    layer: Layer
) {

    val bounds = layer.calculateBounds()


    Canvas(
        modifier = Modifier.size(50.dp)
    ) {


        if (bounds == null) {
            return@Canvas
        }


        /*
            Делаем квадрат вокруг рисунка
        */

        val size = max(
            bounds.width,
            bounds.height
        )


        val centerX =
            bounds.left + bounds.width / 2f

        val centerY =
            bounds.top + bounds.height / 2f


        val squareLeft =
            centerX - size / 2f

        val squareTop =
            centerY - size / 2f



        /*
            Отступ внутри миниатюры
        */

        val padding = 5f


        val scale =

            (this.size.minDimension - padding * 2) /
                    size



        layer.objects.forEach { obj ->


            if (obj is DrawStroke) {


                val path = Path()


                obj.points.forEachIndexed { index, point ->


                    val x =
                        (point.x - squareLeft) * scale + padding


                    val y =
                        (point.y - squareTop) * scale + padding



                    if (index == 0) {

                        path.moveTo(
                            x,
                            y
                        )

                    } else {

                        path.lineTo(
                            x,
                            y
                        )

                    }


                }


                drawPath(

                    path = path,

                    color = obj.color.copy(
                        alpha = obj.opacity * layer.opacity
                    ),

                    style = Stroke(

                        width = obj.width / 10,

                        cap = StrokeCap.Round

                    )

                )

            }


        }


    }

}