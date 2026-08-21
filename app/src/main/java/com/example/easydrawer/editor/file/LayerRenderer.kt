package com.example.easydrawer.file

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import com.example.easydrawer.editor.Layer
import com.example.easydrawer.ui.canvas.DrawStroke

class LayerRenderer {

    fun render(
        layer: Layer,
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
            android.graphics.PorterDuff.Mode.CLEAR
        )

        if (!layer.visible) {
            return bitmap
        }

        for (obj in layer.objects) {

            if (obj is DrawStroke) {

                if (obj.points.isEmpty()) {
                    continue
                }

                val path =
                    Path()

                val first =
                    obj.points.first()

                path.moveTo(
                    first.x,
                    first.y
                )

                for (
                point in
                obj.points.drop(1)
                ) {

                    path.lineTo(
                        point.x,
                        point.y
                    )
                }

                val paint =
                    Paint(
                        Paint.ANTI_ALIAS_FLAG
                    ).apply {

                        style =
                            Paint.Style.STROKE

                        strokeWidth =
                            obj.width

                        strokeCap =
                            Paint.Cap.ROUND

                        strokeJoin =
                            Paint.Join.ROUND

                        color =
                            obj.color
                                .copy(
                                    alpha =
                                        (
                                                obj.opacity *
                                                        layer.opacity *
                                                        255f
                                                )
                                            .toInt()
                                            .coerceIn(
                                                0,
                                                255
                                            ).toFloat()
                                )
                                .toArgb()
                    }

                canvas.drawPath(
                    path,
                    paint
                )
            }
        }

        return bitmap
    }
}