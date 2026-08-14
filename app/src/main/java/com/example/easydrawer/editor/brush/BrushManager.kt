package com.example.easydrawer.editor.brush


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class BrushManager {

    val currentBrushId: String

        get() = selectedBrush?.id ?: "basic"


    val brushes =
        mutableStateListOf<Brush>()



    var selectedBrush by mutableStateOf<Brush?>(null)



    init {

        val basic =
            Brush(

                id = "basic",

                name = "Basic Brush",

                size = 8f,

                brushColor = BrushColor(

                    hue = 0f,

                    saturation = 0f,

                    brightness = 0f,

                    alpha = 1f

                ),

                type = BrushType.BASIC

            )


        brushes.add(basic)

        selectedBrush = basic

    }

    fun updateSelectedBrush(
        update: (Brush) -> Brush
    ) {

        selectedBrush?.let { brush ->

            val updated =
                update(brush)

            selectedBrush = updated


            val index =
                brushes.indexOfFirst {
                    it.id == updated.id
                }


            if (index >= 0) {

                brushes[index] = updated

            }

        }

    }



    fun addBrush(
        brush: Brush
    ) {

        brushes.add(
            brush
        )

    }



    fun removeBrush(
        id:String
    ){

        brushes.removeAll {
            it.id == id
        }

    }


}