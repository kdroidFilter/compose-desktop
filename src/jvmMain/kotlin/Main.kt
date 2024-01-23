import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.inputStream

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Click me!") }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { text = "Magnificent job!" }) {
            Text(text)
        }
    }
}

fun main() {
    val version = System.getProperty("app.version") ?: "Development"
    application {
        // app.dir is set when packaged to point at our collected inputs.
        val appIcon = remember {
            System.getProperty("app.dir")
                ?.let { Paths.get(it, "icon-512.png") }
                ?.takeIf { it.exists() }
                ?.inputStream()
                ?.buffered()
                ?.use { BitmapPainter(loadImageBitmap(it)) }
        }

        Window(onCloseRequest = ::exitApplication, icon = appIcon, title = "Conveyor Compose for Desktop sample $version") {
            App()
        }
    }
}
