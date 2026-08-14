package com.example.easydrawer.ui.brush

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.easydrawer.editor.EditorState
import com.example.easydrawer.ui.brush.color.ColorScreen

@Composable
fun BrushSectionScreen(
    editorState: EditorState,
    section: BrushSection,
    onBack: () -> Unit
) {

    Column {

        Button(
            onClick = onBack
        ) {
            Text("← Back")
        }

        when (section) {

            BrushSection.COLOR -> {

                ColorScreen(
                    editorState = editorState
                )

            }

            BrushSection.DYNAMICS -> {
                Text("Dynamics settings")
            }

            BrushSection.SHAPE -> {
                Text("Shape settings")
            }

            BrushSection.TEXTURE -> {
                Text("Texture settings")
            }

        }

    }

}