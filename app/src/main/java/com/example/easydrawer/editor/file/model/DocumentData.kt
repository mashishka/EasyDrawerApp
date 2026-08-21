package com.example.easydrawer.file.model

data class DocumentData(
    val width: Int,
    val height: Int,
    val layers: List<LayerData>
)