package com.example.easydrawer.ui.layers

import androidx.compose.ui.geometry.Rect
import com.example.easydrawer.editor.Layer
import com.example.easydrawer.ui.canvas.DrawStroke


fun Layer.calculateBounds(): Rect? {

    val strokes = objects
        .filterIsInstance<DrawStroke>()


    if (strokes.isEmpty()) {
        return null
    }


    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE

    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE


    strokes.forEach { stroke ->

        stroke.points.forEach { point ->

            if (point.x < minX)
                minX = point.x

            if (point.y < minY)
                minY = point.y

            if (point.x > maxX)
                maxX = point.x

            if (point.y > maxY)
                maxY = point.y

        }

    }


    return Rect(

        left = minX,

        top = minY,

        right = maxX,

        bottom = maxY

    )

}