package com.example.easydrawer.file

class FileManager(
    private val repository: FileRepository
) {

    var currentFile: EasyDrawerFile? = null
        private set

    fun newFile() {

        currentFile =
            repository.createNew()

    }

    fun open(path: String): Boolean {

        val file =
            repository.open(path)
                ?: return false

        currentFile = file

        return true
    }

    fun save(): Boolean {

        val file =
            currentFile
                ?: return false

        return repository.save(file)
    }

    fun saveAs(path: String): Boolean {

        val file =
            currentFile
                ?: repository.createNew()

        val saved =
            repository.saveAs(
                file,
                path
            )
                ?: return false

        currentFile = saved

        return true
    }
}