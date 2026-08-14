package com.example.easydrawer.ui.brush


import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun BrushSectionButton(
    title:String,
    opened:Boolean,
    onClick:()->Unit
){

    TextButton(
        onClick = onClick
    ){

        Text(
            if(opened)
                "▼ $title"
            else
                "> $title"
        )

    }

}