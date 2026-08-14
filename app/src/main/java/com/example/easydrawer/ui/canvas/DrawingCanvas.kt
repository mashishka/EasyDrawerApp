package com.example.easydrawer.ui.canvas

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.example.easydrawer.editor.EditorState
import androidx.compose.ui.geometry.Offset
import com.example.easydrawer.editor.history.AddStrokeAction
import com.example.easydrawer.editor.brush.BrushManager



@Composable
fun DrawingCanvas(
    editorState: EditorState
) {

    var currentStroke by remember {
        mutableStateOf<DrawStroke?>(null)
    }

    var redrawCounter by remember {
        mutableIntStateOf(0)
    }


    Canvas(

        modifier = Modifier
            .fillMaxSize()

            .pointerInteropFilter { event ->

                when (event.actionMasked) {


                    MotionEvent.ACTION_DOWN -> {


                        editorState.beginStroke()


                        val path = Path()


                        path.moveTo(
                            event.x,
                            event.y
                        )


                        path.lineTo(
                            event.x,
                            event.y
                        )


                        val stroke = DrawStroke(
                            color = editorState.brush.color,

                            width = editorState.brush.size,

                            opacity = editorState.brush.brushColor.alpha,

                            brushId = editorState.brush.id,
                        )

                        stroke.points.add(
                            Offset(
                                event.x,
                                event.y
                            )
                        )


                        editorState.addObject(stroke)

                        editorState.pushAction(
                            AddStrokeAction(stroke)
                        )


                        currentStroke = stroke


                        redrawCounter++


                        true
                    }


                    MotionEvent.ACTION_MOVE -> {


                        currentStroke
                            ?.points
                            ?.add(

                                Offset(
                                    event.x,
                                    event.y
                                )

                            )


                        redrawCounter++


                        true
                    }


                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> {


                        currentStroke = null


                        redrawCounter++


                        true
                    }


                    else -> {

                        true

                    }

                }

            }

    ) {


        redrawCounter


        //editorState.strokes.forEach {


        editorState.document.layers.forEach { layer ->

            if (layer.visible) {

                layer.objects.forEach { obj ->

                    if (obj is DrawStroke) {

                        val stroke = obj

                        val path = Path()

                        if (stroke.points.isNotEmpty()) {

                            path.moveTo(
                                stroke.points.first().x,
                                stroke.points.first().y
                            )

                            for (point in stroke.points.drop(1)) {

                                path.lineTo(
                                    point.x,
                                    point.y
                                )

                            }

                        }

                        drawPath(

                            path = path,

                            color = stroke.color.copy(
                                alpha = stroke.opacity * layer.opacity
                            ),

                            style = Stroke(
                                width = stroke.width,
                                cap = StrokeCap.Round
                            )

                        )

                    }

                }
            }
        }

    }

}