package ui.screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import viewmodel.MainViewModel

@Composable
fun License(vm: MainViewModel) {
    val state = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        LazyColumn(
            Modifier.fillMaxSize(),
            state,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                val textState = rememberRichTextState()
                textState.setText(vm.getLicense()!!)
                RichText(textState, textAlign = TextAlign.Justify)
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            ),
        )
    }
}