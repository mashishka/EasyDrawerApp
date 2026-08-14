package com.example.easydrawer.file.model

data class DrawStrokeData(
    val points: List<PointData>,
    val color: Long,
    val width: Float,
    val opacity: Float,
    val brushId: String
)

data class PointData(
    val x: Float,
    val y: Float
)