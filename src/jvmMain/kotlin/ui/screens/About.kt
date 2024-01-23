package ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import utils.stringResource
import viewmodel.MainViewModel

@Composable
fun About(vm: MainViewModel) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            val state = rememberRichTextState()
            state.setMarkdown(vm.getReadme())
            RichText(state)
        }

        item {
            Text(stringResource("version") + vm.currentVersion)
        }

    }
}