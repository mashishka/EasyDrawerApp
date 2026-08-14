package com.example.easydrawer.ui.brush

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.brush.Brush

@Composable
fun BrushHeader(
    brush: Brush
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        BrushPreview(
            brushSize = brush.size,
            color = brush.color,
            opacity = brush.brushColor.alpha
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = brush.name
        )

    }

}