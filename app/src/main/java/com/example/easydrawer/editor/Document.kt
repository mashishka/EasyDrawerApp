package com.example.easydrawer.editor

import androidx.compose.runtime.mutableStateListOf

class Document(
    val width: Int = 1920,
    val height: Int = 1080
) {

    val layers =
        mutableStateListOf<Layer>()
}