package com.example.easydrawer.editor.export

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore

class PngExporter(
    private val context: Context
) {

    fun export(
        bitmap: Bitmap,
        fileName: String = "EasyDrawer.png"
    ): Boolean {

        val values =
            ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    fileName
                )

                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/png"
                )

                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "Pictures/EasyDrawer"
                )
            }

        val uri =
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
                ?: return false

        return try {

            context.contentResolver
                .openOutputStream(uri)
                ?.use { outputStream ->

                    bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        outputStream
                    )
                }
                ?: false

        } catch (e: Exception) {

            context.contentResolver
                .delete(
                    uri,
                    null,
                    null
                )

            false
        }
    }
}