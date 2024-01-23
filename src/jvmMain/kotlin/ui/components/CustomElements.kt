package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.awt.Cursor

val PointerModifier = Modifier.pointerHoverIcon(
    PointerIcon(
        Cursor.getPredefinedCursor(
            Cursor.HAND_CURSOR
        )
    )
)

@Composable
fun SubTitle(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun RowLabel(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun InfoContainer(text: String, background : Color = MaterialTheme.colorScheme.tertiaryContainer) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(unbounded = true)
            .heightIn(min = 50.dp)
            .background(
                color = background,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) { Text(text = text, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify) }
}



