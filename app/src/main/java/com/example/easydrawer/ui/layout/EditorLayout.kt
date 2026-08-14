package com.example.easydrawer.ui.layout

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.EditorState
import com.example.easydrawer.file.AndroidFileRepository
import com.example.easydrawer.file.DocumentFileConverter
import com.example.easydrawer.file.FileSaver
import com.example.easydrawer.ui.brush.BrushPanel
import com.example.easydrawer.ui.canvas.DrawingCanvas
import com.example.easydrawer.ui.layers.LayersPanel

@Composable
fun EditorLayout(
    config: LayoutConfig,
    editorState: EditorState
) {

    val context = LocalContext.current

    val repository = remember {
        AndroidFileRepository(context)
    }

    val fileSaver = remember {
        FileSaver(repository)
    }

    val saveLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.CreateDocument(
                    "application/json"
                )
        ) { uri ->

            if (uri != null) {

                val json =
                    DocumentFileConverter.encode(
                        editorState.document
                    )

                fileSaver.save(
                    uri = uri,
                    documentJson = json
                )
            }
        }
    val openLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {

                val json =
                    context.contentResolver
                        .openInputStream(uri)
                        ?.bufferedReader()
                        ?.use { reader ->
                            reader.readText()
                        }

                if (json != null) {

                    val document =
                        DocumentFileConverter.decode(
                            json
                        )

                    editorState.loadDocument(
                        document
                    )
                }
            }
        }
    val leftPanel = config.panels.firstOrNull {
        it.side == DockSide.LEFT && it.visible
    }

    var leftPanelVisible by remember {
        mutableStateOf(true)
    }

    var brushPanelVisible by remember {
        mutableStateOf(true)
    }

    val panelWidth by animateDpAsState(
        targetValue =
            if (leftPanelVisible)
                leftPanel?.width?.dp ?: 0.dp
            else
                0.dp,
        label = "panelWidth"
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(
                enabled = editorState.canUndo,
                onClick = {
                    editorState.undo()
                }
            ) {
                Text("Undo")
            }

            Button(
                enabled = editorState.canRedo,
                onClick = {
                    editorState.redo()
                }
            ) {
                Text("Redo")
            }

            Button(
                onClick = {
                    editorState.clear()
                }
            ) {
                Text("Clear")
            }

            Button(
                onClick = {
                    saveLauncher.launch(
                        "drawing.easydrawer"
                    )
                }
            ) {
                Text("Save")
            }
            Button(
                onClick = {
                    openLauncher.launch(
                        arrayOf("application/json")
                    )
                }
            ) {
                Text("Open")
            }
        }

        Row(
            modifier = Modifier.weight(1f)
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                DrawingCanvas(
                    editorState = editorState
                )

                if (leftPanel != null) {

                    Row {

                        Box(
                            modifier = Modifier
                                .width(panelWidth)
                                .fillMaxHeight()
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                                .pointerInput(Unit) {

                                    detectHorizontalDragGestures {
                                            _,
                                            drag ->

                                        if (drag < -20f) {
                                            leftPanelVisible = false
                                        }
                                    }
                                }
                        ) {

                            LayersPanel(
                                editorState = editorState
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .fillMaxHeight()
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                                .pointerInput(Unit) {

                                    detectHorizontalDragGestures {
                                            _,
                                            drag ->

                                        if (drag > 20f) {
                                            leftPanelVisible = true
                                        }
                                    }
                                }
                                .clickable {
                                    leftPanelVisible =
                                        !leftPanelVisible
                                },
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                if (leftPanelVisible)
                                    "❮"
                                else
                                    "❯"
                            )
                        }
                    }
                }

                if (brushPanelVisible) {

                    Box(
                        modifier = Modifier
                            .width(220.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd)
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer
                                )
                        ) {

                            BrushPanel(
                                editorState = editorState
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .fillMaxHeight()
                                .align(Alignment.CenterStart)
                                .offset(x = (-24).dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                                .clickable {
                                    brushPanelVisible = false
                                },
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text("❯")
                        }
                    }

                } else {

                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer
                            )
                            .clickable {
                                brushPanelVisible = true
                            },
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text("❮")
                    }
                }
            }
        }
    }
}