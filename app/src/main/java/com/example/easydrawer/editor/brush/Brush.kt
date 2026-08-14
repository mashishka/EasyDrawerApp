package com.example.easydrawer.editor.brush

import androidx.compose.ui.graphics.Color

data class Brush(

    val id: String,

    val name: String,

    val size: Float,

    val brushColor: BrushColor,

    val type: BrushType,


) {

    val color: Color
        get() = Color.hsv(
            hue = brushColor.hue,
            saturation = brushColor.saturation,
            value = brushColor.brightness,
            alpha = brushColor.alpha
        )

}