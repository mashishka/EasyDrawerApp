package com.example.easydrawer.file

import com.example.easydrawer.editor.Document
import com.example.easydrawer.file.model.DocumentData
import org.json.JSONObject

object DocumentFileConverter {

    fun encode(document: Document): String {
        return EasyDrawerJson.encode(
            document.toData()
        )
    }

    fun decode(json: String): Document {

        val root = JSONObject(json)

        val width =
            root.optInt(
                "width",
                1920
            )

        val height =
            root.optInt(
                "height",
                1080
            )

        val layersJson =
            root.getJSONArray("layers")

        val layers =
            mutableListOf<com.example.easydrawer.file.model.LayerData>()

        for (i in 0 until layersJson.length()) {

            val layerJson =
                layersJson.getJSONObject(i)

            val objectsJson =
                layerJson.getJSONArray("objects")

            val objects =
                mutableListOf<com.example.easydrawer.file.model.DrawObjectData>()

            for (j in 0 until objectsJson.length()) {

                val objectJson =
                    objectsJson.getJSONObject(j)

                when (
                    objectJson.getString("type")
                ) {

                    "stroke" -> {

                        val pointsJson =
                            objectJson.getJSONArray("points")

                        val points =
                            mutableListOf<com.example.easydrawer.file.model.PointData>()

                        for (k in 0 until pointsJson.length()) {

                            val pointJson =
                                pointsJson.getJSONObject(k)

                            points.add(
                                com.example.easydrawer.file.model.PointData(
                                    x = pointJson
                                        .getDouble("x")
                                        .toFloat(),

                                    y = pointJson
                                        .getDouble("y")
                                        .toFloat()
                                )
                            )
                        }

                        objects.add(
                            com.example.easydrawer.file.model.DrawObjectData.Stroke(

                                com.example.easydrawer.file.model.DrawStrokeData(
                                    points = points,

                                    color =
                                        objectJson
                                            .getLong("color"),

                                    width =
                                        objectJson
                                            .getDouble("width")
                                            .toFloat(),

                                    opacity =
                                        objectJson
                                            .getDouble("opacity")
                                            .toFloat(),

                                    brushId =
                                        objectJson
                                            .getString("brushId")
                                )
                            )
                        )
                    }
                }
            }

            layers.add(
                com.example.easydrawer.file.model.LayerData(
                    name =
                        layerJson.getString("name"),

                    visible =
                        layerJson.getBoolean("visible"),

                    opacity =
                        layerJson
                            .getDouble("opacity")
                            .toFloat(),

                    objects = objects
                )
            )
        }

        return DocumentData(
            width = width,
            height = height,
            layers = layers
        ).toDocument()
    }
}