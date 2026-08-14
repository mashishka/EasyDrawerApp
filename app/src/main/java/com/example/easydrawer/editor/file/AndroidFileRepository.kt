package com.example.easydrawer.file

import android.content.Context
import android.net.Uri

class AndroidFileRepository(
    private val context: Context
) {

    fun write(
        uri: Uri,
        documentJson: String
    ): Boolean {

        return try {

            context.contentResolver
                .openOutputStream(uri)
                ?.use { output ->

                    output.write(
                        documentJson.toByteArray(
                            Charsets.UTF_8
                        )
                    )
                }

            true

        } catch (
            e: Exception
        ) {

            false
        }
    }

    fun read(
        uri: Uri
    ): String? {

        return try {

            context.contentResolver
                .openInputStream(uri)
                ?.use { input ->

                    input.readBytes()
                        .toString(
                            Charsets.UTF_8
                        )
                }

        } catch (
            e: Exception
        ) {

            null
        }
    }
}