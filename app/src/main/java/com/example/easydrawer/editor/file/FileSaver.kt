package com.example.easydrawer.file

import android.net.Uri

class FileSaver(
    private val repository: AndroidFileRepository
) {

    fun save(
        uri: Uri,
        documentJson: String
    ): Boolean {

        return repository.write(
            uri = uri,
            documentJson = documentJson
        )
    }
}