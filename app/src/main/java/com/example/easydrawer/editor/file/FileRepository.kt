package com.example.easydrawer.file

interface FileRepository {

    fun createNew(): EasyDrawerFile

    fun open(path: String): EasyDrawerFile?

    fun save(file: EasyDrawerFile): Boolean

    fun saveAs(
        file: EasyDrawerFile,
        path: String
    ): EasyDrawerFile?
}