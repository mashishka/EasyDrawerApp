package com.example.easydrawer.file.model

data class LayerData(
    val name: String,
    val visible: Boolean,
    val opacity: Float,
    val objects: List<DrawObjectData>
)