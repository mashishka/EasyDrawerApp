package com.example.easydrawer.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.easydrawer.ui.canvas.DrawStroke
import com.example.easydrawer.editor.history.EditorAction
import com.example.easydrawer.editor.history.ClearCanvasAction
import com.example.easydrawer.editor.history.AddStrokeAction
import com.example.easydrawer.ui.canvas.DrawObject
import androidx.compose.runtime.mutableIntStateOf
import com.example.easydrawer.editor.BrushSettings
import com.example.easydrawer.editor.brush.Brush
import com.example.easydrawer.editor.brush.BrushManager

class EditorState {

    val brushManager = BrushManager()

    val brush: Brush
        get() = brushManager.selectedBrush!!

    val strokes =
        mutableStateListOf<DrawStroke>()

    private val history =
        mutableStateListOf<EditorAction>()

    private val redoHistory =
        mutableStateListOf<EditorAction>()
    val document = Document()

    init {

        document.layers.add(
            Layer("Layer 1")
        )

    }


//    var brush by mutableStateOf(
//        BrushSettings()
//    )
    val currentLayer: Layer

        get() = document.layers[activeLayerIndex]


    var activeLayerIndex by mutableIntStateOf(0)

    fun beginStroke() {

    }
    fun addObject(
        obj: DrawObject
    ) {

        currentLayer.objects.add(obj)

    }

    fun pushAction(
        action: EditorAction
    ) {

        history.add(action)

        redoHistory.clear()

    }

    fun undo() {

        if (history.isEmpty())
            return

        val action =
            history.removeAt(
                history.lastIndex
            )

        action.undo(this)

        redoHistory.add(action)

    }

    fun redo() {

        if (redoHistory.isEmpty())
            return

        val action =
            redoHistory.removeAt(
                redoHistory.lastIndex
            )

        action.redo(this)

        history.add(action)

    }

    fun clear() {

        if (currentLayer.objects.isEmpty())
            return

        val action = ClearCanvasAction(

            currentLayer.objects.toList()

        )

        action.redo(this)

        pushAction(action)

    }

    fun loadDocument(
        newDocument: Document
    ) {

        document.layers.clear()

        document.layers.addAll(
            newDocument.layers
        )

        activeLayerIndex = 0
    }
    fun addLayer() {

        document.layers.add(
            Layer(
                "Layer ${document.layers.size + 1}"
            )
        )

        activeLayerIndex =
            document.layers.lastIndex

    }
    fun removeCurrentLayer() {

        if (document.layers.size <= 1)
            return

        document.layers.removeAt(
            activeLayerIndex
        )

        activeLayerIndex =
            0

    }
    fun selectLayer(index: Int) {

        if (index !in document.layers.indices)
            return

        activeLayerIndex = index

    }
    fun moveLayer(
        from: Int,
        to: Int
    ) {

        if (from == to)
            return

        if (from !in document.layers.indices)
            return

        if (to !in document.layers.indices)
            return

        val layer =
            document.layers.removeAt(from)

        document.layers.add(
            to,
            layer
        )

        when {

            activeLayerIndex == from ->
                activeLayerIndex = to

            from < activeLayerIndex &&
                    to >= activeLayerIndex ->
                activeLayerIndex--

            from > activeLayerIndex &&
                    to <= activeLayerIndex ->
                activeLayerIndex++

        }

    }
    fun moveLayerUp(index: Int) {

        if (index >= document.layers.lastIndex)
            return


        val layer =
            document.layers.removeAt(index)


        document.layers.add(
            index + 1,
            layer
        )


        activeLayerIndex = index + 1

    }
    fun moveLayerDown(index: Int) {

        if (index <= 0)
            return


        val layer =
            document.layers.removeAt(index)


        document.layers.add(
            index - 1,
            layer
        )


        activeLayerIndex = index - 1

    }

    val canUndo: Boolean

        get() = history.isNotEmpty()

    val canRedo: Boolean

        get() = redoHistory.isNotEmpty()

}