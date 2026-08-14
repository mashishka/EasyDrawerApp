package com.example.easydrawer.editor.history

import com.example.easydrawer.editor.EditorState
import com.example.easydrawer.ui.canvas.DrawStroke

class AddStrokeAction(

    private val stroke: DrawStroke

) : EditorAction {


    override fun undo(
        state: EditorState
    ) {

        state.currentLayer.objects.remove(
            stroke
        )

    }


    override fun redo(
        state: EditorState
    ) {

        state.currentLayer.objects.add(
            stroke
        )

    }

}