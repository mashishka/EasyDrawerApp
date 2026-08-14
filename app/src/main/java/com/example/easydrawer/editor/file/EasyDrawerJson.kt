package com.example.easydrawer.file

import com.example.easydrawer.file.model.DocumentData
import org.json.JSONArray
import org.json.JSONObject

object EasyDrawerJson {

    fun encode(document: DocumentData): String {

        val root = JSONObject()

        root.put("version", 1)

        val layers = JSONArray()

        document.layers.forEach { layer ->

            val layerJson = JSONObject()

            layerJson.put("name", layer.name)
            layerJson.put("visible", layer.visible)
            layerJson.put("opacity", layer.opacity)

            val objects = JSONArray()

            layer.objects.forEach { obj ->

                when (obj) {

                    is com.example.easydrawer.file.model.DrawObjectData.Stroke -> {

                        val stroke = obj.stroke

                        val objectJson = JSONObject()

                        objectJson.put(
                            "type",
                            "stroke"
                        )

                        objectJson.put(
                            "color",
                            stroke.color
                        )

                        objectJson.put(
                            "width",
                            stroke.width
                        )

                        objectJson.put(
                            "opacity",
                            stroke.opacity
                        )

                        objectJson.put(
                            "brushId",
                            stroke.brushId
                        )

                        val points = JSONArray()

                        stroke.points.forEach { point ->

                            val pointJson =
                                JSONObject()

                            pointJson.put(
                                "x",
                                point.x
                            )

                            pointJson.put(
                                "y",
                                point.y
                            )

                            points.put(
                                pointJson
                            )
                        }

                        objectJson.put(
                            "points",
                            points
                        )

                        objects.put(
                            objectJson
                        )
                    }
                }
            }

            layerJson.put(
                "objects",
                objects
            )

            layers.put(
                layerJson
            )
        }

        root.put(
            "layers",
            layers
        )

        return root.toString(2)
    }
}