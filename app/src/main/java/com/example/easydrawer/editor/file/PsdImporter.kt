package com.example.easydrawer.file

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.example.easydrawer.editor.Document
import com.example.easydrawer.editor.Layer
import com.example.easydrawer.ui.canvas.DrawBitmap

class PsdImporter {

    fun import(
        data: PsdDocumentData
    ): Document {

        Log.d(
            "PSD_IMPORT",
            "Document: ${data.width}x${data.height}"
        )

        Log.d(
            "PSD_IMPORT",
            "Layers: ${data.layers.size}"
        )

        val document =
            Document(
                width = data.width,
                height = data.height
            )

        document.layers.clear()

        data.layers.forEachIndexed { index, layerData ->

            Log.d(
                "PSD_IMPORT",
                "Layer $index: " +
                        "name=${layerData.name}, " +
                        "visible=${layerData.visible}, " +
                        "opacity=${layerData.opacity}, " +
                        "rect=${layerData.left},${layerData.top} " +
                        "${layerData.right}x${layerData.bottom}, " +
                        "pixels=${layerData.pixels.size}"
            )

            val width =
                layerData.right -
                        layerData.left

            val height =
                layerData.bottom -
                        layerData.top

            if (width <= 0 || height <= 0) {
                return@forEachIndexed
            }

            val bitmap =
                Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888
                )

            bitmap.setPixels(
                layerData.pixels,
                0,
                width,
                0,
                0,
                width,
                height
            )

            val layer =
                Layer(
                    layerData.name
                )

            layer.visible =
                layerData.visible

            layer.opacity =
                layerData.opacity

            layer.objects.add(
                DrawBitmap(
                    bitmap = bitmap,
                    position = Offset(
                        layerData.left.toFloat(),
                        layerData.top.toFloat()
                    )
                )
            )

            document.layers.add(layer)
        }

        Log.d(
            "PSD_IMPORT",
            "Imported layers: ${document.layers.size}"
        )

        return document
    }
}