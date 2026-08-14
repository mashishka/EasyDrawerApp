package com.example.easydrawer.ui.canvas

import androidx.compose.ui.geometry.Offset

data class DrawSegment(
    val start: Offset,
    val end: Offset
)