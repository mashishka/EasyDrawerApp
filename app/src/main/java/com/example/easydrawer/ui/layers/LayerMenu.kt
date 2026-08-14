package com.example.easydrawer.ui.layers

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.easydrawer.editor.Layer


@Composable
fun LayerMenu(
    layer: Layer
) {

    var opened by remember {
        mutableStateOf(false)
    }


    IconButton(

        onClick = {
            opened = true
        }

    ){

        Text("⋮")

    }



    if(opened){

        AlertDialog(

            onDismissRequest = {
                opened = false
            },


            title = {
                Text("Layer settings")
            },


            text = {


                Column {


                    TextField(

                        value = layer.name,

                        onValueChange = {
                            layer.name = it
                        },

                        label = {
                            Text("Name")
                        }

                    )


                    Text(
                        "Opacity"
                    )


                    Slider(

                        value = layer.opacity,

                        onValueChange = {
                            layer.opacity = it
                        }

                    )

                }


            },


            confirmButton = {

                Button(

                    onClick = {
                        opened = false
                    }

                ){

                    Text("OK")

                }

            }

        )

    }

}