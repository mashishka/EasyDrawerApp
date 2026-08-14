package com.example.easydrawer.ui.brush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.EditorState


@Composable
fun ColorSection(
    editorState: EditorState
) {

    val brush = editorState.brush


    Column {


        Text(
            text = "Color"
        )


        Spacer(
            Modifier.height(8.dp)
        )


        BrushColorWheel(
            brushColor = brush.brushColor,

            onColorChanged = {

                editorState.brushManager
                    .updateSelectedBrush { old ->

                        old.copy(
                            brushColor = it
                        )

                    }

            }
        )


    }

}