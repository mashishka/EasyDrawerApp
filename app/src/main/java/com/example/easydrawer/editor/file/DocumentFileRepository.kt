package com.example.easydrawer.file

import android.content.Context
import com.example.easydrawer.editor.Document
import java.io.File

class DocumentFileRepository(
    private val context: Context
) {

    private val documentsDirectory: File
        get() {
            val directory = File(
                context.filesDir,
                "documents"
            )

            if (!directory.exists()) {
                directory.mkdirs()
            }

            return directory
        }

    fun save(
        document: Document,
        fileName: String
    ): File {

        val name =
            if (fileName.endsWith(".json")) {
                fileName
            } else {
                "$fileName.json"
            }

        val file = File(
            documentsDirectory,
            name
        )

        file.writeText(
            DocumentFileConverter.encode(document)
        )

        return file
    }

    fun load(
        file: File
    ): Document {

        val json =
            file.readText()

        return DocumentFileConverter.decode(
            json
        )
    }

    fun listDocuments(): List<File> {

        return documentsDirectory
            .listFiles()
            ?.filter {
                it.isFile &&
                        it.extension.equals(
                            "json",
                            ignoreCase = true
                        )
            }
            ?.sortedBy {
                it.name.lowercase()
            }
            ?: emptyList()
    }

    fun delete(
        file: File
    ) {

        if (file.exists()) {
            file.delete()
        }
    }
}