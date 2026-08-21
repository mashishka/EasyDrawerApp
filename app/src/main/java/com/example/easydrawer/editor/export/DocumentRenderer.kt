package com.example.easydrawer.editor.export

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import com.example.easydrawer.editor.Document
import com.example.easydrawer.ui.canvas.DrawStroke

class DocumentRenderer {

    fun render(
        document: Document,
        width: Int,
        height: Int
    ): Bitmap {

        val bitmap =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

        val canvas =
            Canvas(bitmap)

        canvas.drawColor(
            android.graphics.Color.TRANSPARENT,
            PorterDuff.Mode.CLEAR
        )

        val paint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                strokeCap =
                    Paint.Cap.ROUND

                style =
                    Paint.Style.STROKE
            }

        document.layers.forEach { layer ->

            if (!layer.visible) {
                return@forEach
            }

            layer.objects.forEach { obj ->

                if (obj is DrawStroke) {

                    if (obj.points.isEmpty()) {
                        return@forEach
                    }

                    val path =
                        Path()

                    val first =
                        obj.points.first()

                    path.moveTo(
                        first.x,
                        first.y
                    )

                    obj.points
                        .drop(1)
                        .forEach { point ->

                            path.lineTo(
                                point.x,
                                point.y
                            )
                        }

                    val alpha =
                        (
                                obj.opacity *
                                        layer.opacity *
                                        255f
                                )
                            .toInt()
                            .coerceIn(
                                0,
                                255
                            )

                    paint.color =
                        android.graphics.Color.argb(
                            alpha,
                            (obj.color.red * 255f).toInt(),
                            (obj.color.green * 255f).toInt(),
                            (obj.color.blue * 255f).toInt()
                        )

                    paint.strokeWidth =
                        obj.width

                    canvas.drawPath(
                        path,
                        paint
                    )
                }
            }
        }

        return bitmap
    }
}