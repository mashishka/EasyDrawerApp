package com.example.easydrawer.ui.layout

object LayoutLoader {

    fun load(): LayoutConfig {

        return LayoutConfig(

            panels = listOf(

                PanelConfig(
                    id = "brush",
                    side = DockSide.LEFT,
                    mode = PanelMode.SLIDING,
                    width = 280
                ),

                PanelConfig(
                    id = "layers",
                    side = DockSide.RIGHT,
                    mode = PanelMode.SLIDING,
                    width = 280
                )

            )

        )

    }

}