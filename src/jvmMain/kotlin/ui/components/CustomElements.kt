package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.openUrlInBrowser
import java.awt.Cursor
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.inputStream

@OptIn(ExperimentalResourceApi::class)
@Composable
fun loadAppIcon(): Painter {
    return System.getProperty("app.dir")
        ?.let { Paths.get(it, "icon-512.png") }
        ?.takeIf { it.exists() }
        ?.inputStream()
        ?.buffered()
        ?.use { BitmapPainter(loadImageBitmap(it)) }
        ?: painterResource("AppIcon.png")
}

val PointerModifier = Modifier.pointerHoverIcon(
    PointerIcon(
        Cursor.getPredefinedCursor(
            Cursor.HAND_CURSOR
        )
    )
)

@Composable
fun LinkText(
    text: String,
    url: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    textDecoration: TextDecoration = TextDecoration.Underline
) {
    Text(
        text = text,
        modifier = PointerModifier.clickable {
            openUrlInBrowser(url)
        },
        color = color,
        textDecoration = textDecoration,
        style = style
    )
}

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
fun InfoContainer(text: String, background: Color = MaterialTheme.colorScheme.tertiaryContainer) {
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
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify
        )
    }
}



