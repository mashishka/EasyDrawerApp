package com.example.easydrawer.ui.canvas

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawStroke(

    val points: androidx.compose.runtime.snapshots.SnapshotStateList<Offset> =
        mutableStateListOf(),

    val color: Color,

    val width: Float,

    val opacity: Float = 1f,

    val brushId:String

) : DrawObject