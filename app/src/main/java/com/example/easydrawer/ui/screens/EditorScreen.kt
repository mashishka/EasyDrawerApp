package com.example.easydrawer.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.easydrawer.editor.EditorState
import com.example.easydrawer.ui.layout.DockSide
import com.example.easydrawer.ui.layout.EditorLayout
import com.example.easydrawer.ui.layout.LayoutConfig
import com.example.easydrawer.ui.layout.PanelConfig
import com.example.easydrawer.ui.layout.PanelMode


@Composable
fun EditorScreen() {

    val editorState = remember {
        EditorState()
    }


    val config = LayoutConfig(
        panels = listOf(

            PanelConfig(
                id = "Layers",
                side = DockSide.LEFT,
                mode = PanelMode.DOCKED,
                width = 250
            )

        )
    )


    EditorLayout(
        config = config,
        editorState = editorState
    )

}