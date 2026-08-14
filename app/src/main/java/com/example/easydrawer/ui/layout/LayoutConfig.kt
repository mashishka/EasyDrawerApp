package com.example.easydrawer.ui.layout
import androidx.compose.runtime.*

enum class DockSide {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
}

enum class PanelMode {
    SLIDING,
    DOCKED
}

data class PanelConfig(
    val id: String,
    val side: DockSide,
    val mode: PanelMode,
    val width: Int,
    val visible: Boolean = true
)

data class LayoutConfig(
    val panels: List<PanelConfig>
)