package com.example.easydrawer.ui.layers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.example.easydrawer.editor.Layer
import com.example.easydrawer.ui.canvas.DrawBitmap
import com.example.easydrawer.ui.canvas.DrawStroke
import kotlin.math.max
import kotlin.math.min

object LayerPreviewRenderer {

    private fun findBitmapBounds(
        bitmap: Bitmap
    ): Rect? {

        val width = bitmap.width
        val height = bitmap.height

        val pixels =
            IntArray(width * height)

        bitmap.getPixels(
            pixels,
            0,
            width,
            0,
            0,
            width,
            height
        )

        var left = width
        var top = height
        var right = -1
        var bottom = -1

        for (y in 0 until height) {

            for (x in 0 until width) {

                val alpha =
                    (pixels[y * width + x] ushr 24) and 0xFF

                if (alpha > 5) {

                    left =
                        minOf(left, x)

                    top =
                        minOf(top, y)

                    right =
                        maxOf(right, x)

                    bottom =
                        maxOf(bottom, y)
                }
            }
        }

        if (right < left || bottom < top) {
            return null
        }

        return Rect(
            left,
            top,
            right + 1,
            bottom + 1
        )
    }

    fun render(
        layer: Layer,
        size: Int = 100
    ): Bitmap {

        val result =
            Bitmap.createBitmap(
                size,
                size,
                Bitmap.Config.ARGB_8888
            )

        val canvas =
            Canvas(result)

        val paint =
            Paint(
                Paint.ANTI_ALIAS_FLAG or
                        Paint.FILTER_BITMAP_FLAG or
                        Paint.DITHER_FLAG
            ).apply {
                isFilterBitmap = true
                isDither = true
            }

        /*
         * Находим реальные границы содержимого слоя.
         */
        var left = Float.POSITIVE_INFINITY
        var top = Float.POSITIVE_INFINITY
        var right = Float.NEGATIVE_INFINITY
        var bottom = Float.NEGATIVE_INFINITY

        layer.objects.forEach { obj ->

            when (obj) {

                is DrawStroke -> {

                    obj.points.forEach { point ->

                        left = min(left, point.x)
                        top = min(top, point.y)
                        right = max(right, point.x)
                        bottom = max(bottom, point.y)
                    }
                }

                is DrawBitmap -> {

                    left = min(
                        left,
                        obj.position.x
                    )

                    top = min(
                        top,
                        obj.position.y
                    )

                    right = max(
                        right,
                        obj.position.x +
                                obj.bitmap.width
                    )

                    bottom = max(
                        bottom,
                        obj.position.y +
                                obj.bitmap.height
                    )
                }
            }
        }

        if (
            !left.isFinite() ||
            !top.isFinite() ||
            !right.isFinite() ||
            !bottom.isFinite()
        ) {
            return result
        }

        val contentWidth =
            (right - left).coerceAtLeast(1f)

        val contentHeight =
            (bottom - top).coerceAtLeast(1f)

        /*
         * Делаем квадратную область.
         */
        val squareSize =
            max(
                contentWidth,
                contentHeight
            )

        val centerX =
            (left + right) / 2f

        val centerY =
            (top + bottom) / 2f

        val squareLeft =
            centerX -
                    squareSize / 2f

        val squareTop =
            centerY -
                    squareSize / 2f

        /*
         * Отступ от края thumbnail.
         */
        val padding =
            size * 0.08f

        val availableSize =
            size - padding * 2f

        val scale =
            availableSize / squareSize

        /*
         * Рисуем объекты.
         */
        layer.objects.forEach { obj ->

            when (obj) {

                is DrawStroke -> {

                    if (obj.points.isEmpty()) {
                        return@forEach
                    }

                    val path =
                        android.graphics.Path()

                    obj.points.forEachIndexed {
                            index,
                            point ->

                        val x =
                            padding +
                                    (
                                            point.x -
                                                    squareLeft
                                            ) *
                                    scale

                        val y =
                            padding +
                                    (
                                            point.y -
                                                    squareTop
                                            ) *
                                    scale

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    paint.style =
                        Paint.Style.STROKE

                    paint.color =
                        obj.color.value.toInt()

                    paint.alpha =
                        (
                                obj.opacity *
                                        layer.opacity *
                                        255f
                                )
                            .toInt()
                            .coerceIn(0, 255)

                    paint.strokeWidth =
                        (
                                obj.width *
                                        scale
                                )
                            .coerceAtLeast(1f)

                    paint.strokeCap =
                        Paint.Cap.ROUND

                    paint.strokeJoin =
                        Paint.Join.ROUND

                    canvas.drawPath(
                        path,
                        paint
                    )
                }

                is DrawBitmap -> {

                    val x =
                        padding +
                                (
                                        obj.position.x -
                                                squareLeft
                                        ) *
                                scale

                    val y =
                        padding +
                                (
                                        obj.position.y -
                                                squareTop
                                        ) *
                                scale

                    val dstWidth =
                        (
                                obj.bitmap.width *
                                        scale
                                )
                            .coerceAtLeast(1f)

                    val dstHeight =
                        (
                                obj.bitmap.height *
                                        scale
                                )
                            .coerceAtLeast(1f)

                    val src =
                        Rect(
                            0,
                            0,
                            obj.bitmap.width,
                            obj.bitmap.height
                        )

                    val dst =
                        RectF(
                            x,
                            y,
                            x + dstWidth,
                            y + dstHeight
                        )

                    paint.style =
                        Paint.Style.FILL

                    paint.alpha =
                        (
                                layer.opacity *
                                        255f
                                )
                            .toInt()
                            .coerceIn(0, 255)

                    /*
                     * ВАЖНО:
                     *
                     * Не создаём новый scaled Bitmap.
                     * Android сам делает bilinear filtering
                     * при drawBitmap().
                     */
                    canvas.drawBitmap(
                        obj.bitmap,
                        src,
                        dst,
                        paint
                    )
                }
            }
        }

        return result
    }
}