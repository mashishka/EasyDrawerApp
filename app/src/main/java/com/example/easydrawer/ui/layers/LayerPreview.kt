package com.example.easydrawer.ui.layers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.Layer
import com.example.easydrawer.ui.canvas.DrawBitmap
import com.example.easydrawer.ui.canvas.DrawStroke
import kotlin.math.max
import kotlin.math.min

@Composable
fun LayerPreview(
    layer: Layer
) {
    val bitmap =
        createLayerPreviewBitmap(
            layer = layer,
            previewSize = 400
        )

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.size(50.dp)
    )
}

private fun createLayerPreviewBitmap(
    layer: Layer,
    previewSize: Int
): Bitmap {

    val result =
        Bitmap.createBitmap(
            previewSize,
            previewSize,
            Bitmap.Config.ARGB_8888
        )

    val canvas =
        Canvas(result)

    /*
     * =================================================
     * 1. Сначала определяем bounds всего содержимого
     * =================================================
     */

    var left = Float.POSITIVE_INFINITY
    var top = Float.POSITIVE_INFINITY
    var right = Float.NEGATIVE_INFINITY
    var bottom = Float.NEGATIVE_INFINITY

    layer.objects.forEach { obj ->

        when (obj) {

            is DrawStroke -> {

                obj.points.forEach { point ->

                    left =
                        min(left, point.x)

                    top =
                        min(top, point.y)

                    right =
                        max(right, point.x)

                    bottom =
                        max(bottom, point.y)
                }
            }

            is DrawBitmap -> {

                val bitmapBounds =
                    findBitmapBounds(
                        obj.bitmap
                    )

                if (bitmapBounds != null) {

                    val objectLeft =
                        obj.position.x +
                                bitmapBounds.left

                    val objectTop =
                        obj.position.y +
                                bitmapBounds.top

                    val objectRight =
                        obj.position.x +
                                bitmapBounds.right

                    val objectBottom =
                        obj.position.y +
                                bitmapBounds.bottom

                    left =
                        min(
                            left,
                            objectLeft
                        )

                    top =
                        min(
                            top,
                            objectTop
                        )

                    right =
                        max(
                            right,
                            objectRight
                        )

                    bottom =
                        max(
                            bottom,
                            objectBottom
                        )
                }
            }
        }
    }

    /*
     * Слой пустой.
     */
    if (
        !left.isFinite() ||
        !top.isFinite() ||
        !right.isFinite() ||
        !bottom.isFinite()
    ) {
        return result
    }

    /*
     * =================================================
     * 2. Формируем квадрат вокруг содержимого
     * =================================================
     */

    val contentWidth =
        (right - left)
            .coerceAtLeast(1f)

    val contentHeight =
        (bottom - top)
            .coerceAtLeast(1f)

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
     * =================================================
     * 3. Масштабирование в preview
     * =================================================
     */

    val padding =
        previewSize * 0.08f

    val availableSize =
        previewSize -
                padding * 2f

    val scale =
        availableSize /
                squareSize

    /*
     * =================================================
     * 4. Paint для bitmap
     * =================================================
     */

    val bitmapPaint =
        Paint(
            Paint.ANTI_ALIAS_FLAG or
                    Paint.FILTER_BITMAP_FLAG or
                    Paint.DITHER_FLAG
        ).apply {

            isFilterBitmap = true
            isDither = true
        }

    /*
     * =================================================
     * 5. Рисуем объекты
     * =================================================
     */

    layer.objects.forEach { obj ->

        when (obj) {

            /*
             * -----------------------------------------
             * Stroke
             * -----------------------------------------
             */

            is DrawStroke -> {

                if (obj.points.isEmpty()) {
                    return@forEach
                }

                val path =
                    Path()

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

                val paint =
                    Paint(
                        Paint.ANTI_ALIAS_FLAG or
                                Paint.DITHER_FLAG
                    ).apply {

                        isDither = true

                        style =
                            Paint.Style.STROKE

                        color =
                            obj.color.value
                                .toLong()
                                .toInt()

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
                                )

                        strokeWidth =
                            (
                                    obj.width *
                                            scale
                                    )
                                .coerceAtLeast(
                                    1f
                                )

                        strokeCap =
                            Paint.Cap.ROUND

                        strokeJoin =
                            Paint.Join.ROUND
                    }

                canvas.drawPath(
                    path,
                    paint
                )
            }

            /*
             * -----------------------------------------
             * PSD bitmap
             * -----------------------------------------
             */

            is DrawBitmap -> {

                val bitmapBounds =
                    findBitmapBounds(
                        obj.bitmap
                    )
                        ?: return@forEach

                val x =
                    padding +
                            (
                                    obj.position.x +
                                            bitmapBounds.left -
                                            squareLeft
                                    ) *
                            scale

                val y =
                    padding +
                            (
                                    obj.position.y +
                                            bitmapBounds.top -
                                            squareTop
                                    ) *
                            scale

                val dstWidth =
                    bitmapBounds.width() *
                            scale

                val dstHeight =
                    bitmapBounds.height() *
                            scale

                val dst =
                    RectF(
                        x,
                        y,
                        x + dstWidth,
                        y + dstHeight
                    )

                bitmapPaint.alpha =
                    (
                            layer.opacity *
                                    255f
                            )
                        .toInt()
                        .coerceIn(
                            0,
                            255
                        )

                /*
                 * Рисуем только непрозрачную область
                 * bitmap, а не весь PSD bitmap.
                 */
                canvas.drawBitmap(
                    obj.bitmap,
                    bitmapBounds,
                    dst,
                    bitmapPaint
                )
            }
        }
    }

    return result
}

/*
 * =====================================================
 * Находим реальные границы непрозрачного содержимого
 * bitmap.
 * =====================================================
 */

private fun findBitmapBounds(
    bitmap: Bitmap
): Rect? {

    val width =
        bitmap.width

    val height =
        bitmap.height

    if (
        width <= 0 ||
        height <= 0
    ) {
        return null
    }

    val pixels =
        IntArray(
            width * height
        )

    bitmap.getPixels(
        pixels,
        0,
        width,
        0,
        0,
        width,
        height
    )

    var left =
        width

    var top =
        height

    var right =
        -1

    var bottom =
        -1

    for (y in 0 until height) {

        for (x in 0 until width) {

            val pixel =
                pixels[
                    y * width + x
                ]

            val alpha =
                (pixel ushr 24) and 0xFF

            /*
             * 5 — маленький порог,
             * чтобы почти прозрачные пиксели
             * не раздували bounds.
             */
            if (alpha > 5) {

                left =
                    min(
                        left,
                        x
                    )

                top =
                    min(
                        top,
                        y
                    )

                right =
                    max(
                        right,
                        x
                    )

                bottom =
                    max(
                        bottom,
                        y
                    )
            }
        }
    }

    if (
        right < left ||
        bottom < top
    ) {
        return null
    }

    return Rect(
        left,
        top,
        right + 1,
        bottom + 1
    )
}