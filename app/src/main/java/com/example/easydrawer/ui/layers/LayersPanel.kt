package com.example.easydrawer.ui.layers

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.easydrawer.editor.EditorState


@Composable
fun LayersPanel(
    editorState: EditorState
) {


    Column {
        var draggingIndex by remember {
            mutableIntStateOf(-1)
        }

        var dragOffset by remember {
            mutableFloatStateOf(0f)
        }


        Text(
            text = "Layers"
        )


        editorState.document.layers.forEachIndexed { index, layer ->

            LayerItem(
                layer = layer,

                selected =
                    index == editorState.activeLayerIndex,

                dragging =
                    draggingIndex == index,

                offsetY =
                    if (draggingIndex == index)
                        dragOffset
                    else
                        0f,

                onClick = {
                    editorState.selectLayer(index)
                },

                onDragStart = {
                    draggingIndex = index
                },

                onDrag = { dy ->

                    dragOffset += dy

                    if (dragOffset > 60f &&
                        index < editorState.document.layers.lastIndex
                    ) {

                        editorState.moveLayer(
                            index,
                            index + 1
                        )

                        draggingIndex = index + 1
                        dragOffset = 0f
                    }

                    if (dragOffset < -60f &&
                        index > 0
                    ) {

                        editorState.moveLayer(
                            index,
                            index - 1
                        )

                        draggingIndex = index - 1
                        dragOffset = 0f
                    }

                },

                onDragEnd = {

                    draggingIndex = -1
                    dragOffset = 0f

                }

            )
            /*LayerItem(

                layer = layer,


                selected =
                    index == editorState.activeLayerIndex,


                onClick = {

                    editorState.selectLayer(index)

                },

                onMove = { dy ->


                    if (
                        dy > 50 &&
                        index < editorState.document.layers.lastIndex
                    ) {

                        editorState.moveLayer(
                            index,
                            index + 1
                        )

                    }


                    if (
                        dy < -50 &&
                        index > 0
                    ) {

                        editorState.moveLayer(
                            index,
                            index - 1
                        )

                    }


                }

            )*/


        }



        Button(

            onClick = {

                editorState.addLayer()

            }

        ) {

            Text("+ Layer")

        }



    }

}