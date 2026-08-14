package com.example.easydrawer.editor

import androidx.compose.ui.graphics.Color


data class BrushSettings(

    val size: Float = 8f,

    val color: Color = Color.Black,

    val opacity: Float = 1f,

    val smoothing: Float = 0.5f



)