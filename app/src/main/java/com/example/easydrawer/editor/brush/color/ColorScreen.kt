package com.example.easydrawer.ui.brush.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.EditorState
import com.example.easydrawer.ui.brush.BrushColorPicker

@Composable
fun ColorScreen(
    editorState: EditorState
) {

    val brush = editorState.brush

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Color",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        /*
         * ЕДИНЫЙ COLOR PICKER
         */
        BrushColorPicker(
            brushColor = brush.brushColor,


            onColorChanged = { newColor ->

                editorState.brushManager
                    .updateSelectedBrush { old ->

                        old.copy(
                            brushColor = newColor
                        )

                    }
            }
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        /*
         * ТЕКУЩИЙ ЦВЕТ
         */
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = brush.color,
                    shape = RoundedCornerShape(12.dp)
                )
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

//        /*
//         * OPACITY
//         */
//        Column(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//
//            Text(
//                text =
//                    "Opacity ${
//                        (brush.brushColor.alpha * 100)
//                            .toInt()
//                    }%"
//            )
//
//            Slider(
//                value = brush.brushColor.alpha,
//
//                onValueChange = { value ->
//
//                    editorState.brushManager
//                        .updateSelectedBrush { old ->
//
//                            old.copy(
//                                brushColor =
//                                    old.brushColor.copy(
//                                        alpha = value
//                                    )
//                            )
//
//                        }
//                },
//
//                valueRange = 0f..1f
//            )
//        }
    }
}