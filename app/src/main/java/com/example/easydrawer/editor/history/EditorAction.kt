package com.example.easydrawer.editor.history

import com.example.easydrawer.editor.EditorState

interface EditorAction {

    fun undo(state: EditorState)

    fun redo(state: EditorState)

}