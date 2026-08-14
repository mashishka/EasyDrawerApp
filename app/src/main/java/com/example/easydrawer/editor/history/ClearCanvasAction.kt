package com.example.easydrawer.editor.history

import com.example.easydrawer.editor.EditorState
import com.example.easydrawer.ui.canvas.DrawObject

class ClearCanvasAction(

    private val removed: List<DrawObject>

) : EditorAction {


    override fun undo(
        state: EditorState
    ) {

        state.currentLayer.objects.addAll(
            removed
        )

    }


    override fun redo(
        state: EditorState
    ) {

        state.currentLayer.objects.clear()

    }

}