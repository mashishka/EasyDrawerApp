package com.example.easydrawer.file.model

sealed class DrawObjectData {

    data class Stroke(
        val stroke: DrawStrokeData
    ) : DrawObjectData()
}