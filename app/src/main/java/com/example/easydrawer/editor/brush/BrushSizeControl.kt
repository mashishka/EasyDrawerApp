package com.example.easydrawer.ui.brush

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.easydrawer.editor.EditorState

@Composable
fun BrushSizeControl(
    editorState: EditorState
) {

    val brush = editorState.brush

    Column {

        Text(
            text = "Size ${brush.size.toInt()}"
        )

        Slider(
            value = brush.size,
            onValueChange = { value ->

                editorState.brushManager.updateSelectedBrush { brush ->

                    brush.copy(
                        size = value
                    )

                }

            },
            valueRange = 1f..100f
        )

    }

}