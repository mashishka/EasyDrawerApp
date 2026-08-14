package com.example.easydrawer.ui.brush

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easydrawer.editor.EditorState

@Composable
fun BrushPanel(
    editorState: EditorState
) {

    var selectedSection by remember {
        mutableStateOf<BrushSection?>(null)
    }

    val brush = editorState.brush

    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
    ) {

        /*
         * =========================
         * FIXED HEADER
         * =========================
         */

        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {

            BrushHeader(
                brush = brush
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Divider()

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            BrushControlPanel(
                editorState = editorState
            )

        }


        /*
         * =========================
         * SCROLLABLE CONTENT
         * =========================
         */

        Column(

            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                )

        ) {

            val section = selectedSection

            if (section == null) {

                BrushMenu(

                    selectedSection = null,

                    onSectionSelected = {
                        selectedSection = it
                    }

                )

            } else {

                BrushSectionScreen(

                    editorState = editorState,

                    section = section,

                    onBack = {
                        selectedSection = null
                    }

                )

            }

        }

    }

}