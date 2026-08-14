package com.example.easydrawer.ui.layers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
// import androidx.compose.ui.input.pointer.consume
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.example.easydrawer.editor.Layer

@Composable
fun LayerItem(

    layer: Layer,

    selected: Boolean,

    dragging: Boolean,

    offsetY: Float,

    onClick: () -> Unit,

    onDragStart: () -> Unit,

    onDrag: (Float) -> Unit,

    onDragEnd: () -> Unit

) {

    Row(

        modifier = Modifier

            .fillMaxWidth()

            .height(60.dp)

            .graphicsLayer {

                translationY = offsetY

            }

            .alpha(
                if (dragging) 0.7f else 1f
            )

            .background(

                if (selected)
                    Color(0xFFE3F2FD)
                else
                    Color.Transparent

            )

            .pointerInput(Unit) {

                detectDragGestures(

                    onDragStart = {

                        onDragStart()

                    },

                    onDragEnd = {

                        onDragEnd()

                    },

                    onDragCancel = {

                        onDragEnd()

                    }

                ) { change, dragAmount ->

                    change.consume()

                    onDrag(
                        dragAmount.y
                    )

                }

            }

            .clickable {

                onClick()

            },

        verticalAlignment = Alignment.CenterVertically

    ) {

        LayerPreview(
            layer = layer
        )
        Text(
            text = layer.name
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )


        LayerMenu(
            layer = layer
        )

    }

}