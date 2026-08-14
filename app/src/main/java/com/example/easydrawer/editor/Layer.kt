package com.example.easydrawer.editor

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.easydrawer.ui.canvas.DrawObject


class Layer(
    initialName: String
) {

    var name by mutableStateOf(initialName)


    val objects =
        mutableStateListOf<DrawObject>()


    var visible by mutableStateOf(true)


    var opacity by mutableFloatStateOf(1f)

}