package com.example.easydrawer.ui.canvas

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset

data class DrawBitmap(
    val bitmap: Bitmap,
    val position: Offset = Offset.Zero
) : DrawObject