package com.example.easydrawer.file

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import com.example.easydrawer.editor.Document
import com.example.easydrawer.editor.Layer
import com.example.easydrawer.file.model.DocumentData
import com.example.easydrawer.file.model.LayerData
import com.example.easydrawer.file.model.DrawObjectData
import com.example.easydrawer.file.model.DrawStrokeData
import com.example.easydrawer.file.model.PointData
import com.example.easydrawer.ui.canvas.DrawStroke

fun Document.toData(): DocumentData {

    return DocumentData(
        layers = layers.map { layer ->

            LayerData(
                name = layer.name,
                visible = layer.visible,
                opacity = layer.opacity,

                objects = layer.objects.map { obj ->

                    when (obj) {

                        is DrawStroke -> {

                            DrawObjectData.Stroke(

                                DrawStrokeData(

                                    points =
                                        obj.points.map { point ->

                                            PointData(
                                                x = point.x,
                                                y = point.y
                                            )
                                        },

                                    color =
                                        obj.color.value.toLong(),

                                    width =
                                        obj.width,

                                    opacity =
                                        obj.opacity,

                                    brushId =
                                        obj.brushId
                                )
                            )
                        }

                        else -> {
                            error(
                                "Unsupported DrawObject: ${obj::class}"
                            )
                        }
                    }
                }
            )
        }
    )
}

fun DocumentData.toDocument(): Document {

    val document =
        Document()

    document.layers.clear()

    layers.forEach { layerData ->

        val layer =
            Layer(
                layerData.name
            )

        layer.visible =
            layerData.visible

        layer.opacity =
            layerData.opacity

        layerData.objects.forEach { objectData ->

            when (objectData) {

                is DrawObjectData.Stroke -> {

                    val data =
                        objectData.stroke

                    val stroke =
                        DrawStroke(

                            points =
                                androidx.compose.runtime
                                    .mutableStateListOf<Offset>()
                                    .apply {

                                        addAll(
                                            data.points.map { point ->

                                                Offset(
                                                    point.x,
                                                    point.y
                                                )
                                            }
                                        )
                                    },

                            color =
                                Color(
                                    data.color.toULong()
                                ),

                            width =
                                data.width,

                            opacity =
                                data.opacity,

                            brushId =
                                data.brushId
                        )

                    layer.objects.add(stroke)
                }
            }
        }

        document.layers.add(layer)
    }

    return document
}