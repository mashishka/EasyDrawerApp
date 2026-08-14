package com.example.easydrawer.ui.brush


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.easydrawer.editor.EditorState


@Composable
fun BrushControlPanel(
    editorState: EditorState
){

    val brush =
        editorState.brush


    Column {


        Text(
            "Size ${brush.size.toInt()}"
        )


        Slider(

            value = brush.size,

            onValueChange = {

                editorState.brushManager
                    .updateSelectedBrush { old ->

                        old.copy(
                            size = it
                        )

                    }

            },

            valueRange = 1f..100f

        )



        Text(
            "Opacity ${(brush.brushColor.alpha*100).toInt()}%"
        )


        Slider(

            value = brush.brushColor.alpha,

            onValueChange = {

                editorState.brushManager
                    .updateSelectedBrush { old ->

                        old.copy(

                            brushColor = old.brushColor.copy(
                                alpha = it
                            )

                        )

                    }

            },

            valueRange = 0f..1f

        )

    }

}