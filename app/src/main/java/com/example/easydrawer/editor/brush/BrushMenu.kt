package com.example.easydrawer.ui.brush

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun BrushMenu(
    selectedSection: BrushSection?,
    onSectionSelected: (BrushSection) -> Unit
) {

    Column {

        BrushSectionButton(
            title = "🎨 Color",
            opened = selectedSection == BrushSection.COLOR
        ) {
            onSectionSelected(
                BrushSection.COLOR
            )
        }

        BrushSectionButton(
            title = "↔ Dynamics",
            opened = selectedSection == BrushSection.DYNAMICS
        ) {
            onSectionSelected(
                BrushSection.DYNAMICS
            )
        }

        BrushSectionButton(
            title = "◯ Shape",
            opened = selectedSection == BrushSection.SHAPE
        ) {
            onSectionSelected(
                BrushSection.SHAPE
            )
        }

        BrushSectionButton(
            title = "▦ Texture",
            opened = selectedSection == BrushSection.TEXTURE
        ) {
            onSectionSelected(
                BrushSection.TEXTURE
            )
        }

    }

}