package com.example.easydrawer.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

class PngExporter(
    private val context: Context
) {

    fun export(
        bitmap: Bitmap,
        uri: Uri
    ): Boolean {

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

        } catch (
            exception: Exception
        ) {

            false
        }
    }
}