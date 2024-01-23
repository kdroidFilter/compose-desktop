package utils

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource
import viewmodel.MainViewModel
import java.io.ByteArrayInputStream

object ImageCache {
    private val bitmapCache = mutableMapOf<String, ImageBitmap>()
    private val painterCache = mutableMapOf<String, Painter>()
    private val imageVectorCache = mutableMapOf<String, ImageVector>()

    fun getBitmap(url: String): ImageBitmap? = bitmapCache[url]
    fun getPainter(url: String): Painter? = painterCache[url]
    fun getImageVector(url: String): ImageVector? = imageVectorCache[url]

    fun putBitmap(url: String, bitmap: ImageBitmap) {
        bitmapCache[url] = bitmap
    }

    fun putPainter(url: String, painter: Painter) {
        painterCache[url] = painter
    }

    fun putImageVector(url: String, imageVector: ImageVector) {
        imageVectorCache[url] = imageVector
    }
}

@Composable
fun <T> AsyncImage(
    load: suspend () -> T?,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String = "",
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    fadeInDuration: Int = 1000,
    cornerRadius: Dp = 10.dp
) {
    val image: T? by produceState<T?>(initialValue = null) {
        value = withContext(Dispatchers.IO) { load() }
    }

    Crossfade(targetState = image, animationSpec = tween(durationMillis = fadeInDuration)) { img ->
        if (img != null) {
            Image(
                painter = painterFor(img),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
        } else {
            Box(modifier = Modifier
                .then(modifier)
                .background(color = placeholderColor, shape = RoundedCornerShape(cornerRadius)))
        }
    }
}

suspend fun loadImageBitmap(vm: MainViewModel, url: String): ImageBitmap? {
    ImageCache.getBitmap(url)?.let { return it }

    return if (url.isNotBlank()) {
        try {
            urlStream(vm, url).use { stream ->
                loadImageBitmap(stream).also {
                    ImageCache.putBitmap(url, it)
                }
            }
        } catch (e: Exception) {
            println("Erreur lors du chargement de l'image : $e")
            null
        }
    } else {
        null
    }
}

suspend fun loadSvgPainter(vm: MainViewModel, url: String, density: Density): Painter {
    ImageCache.getPainter(url)?.let { return it }

    return urlStream(vm, url).use { stream ->
        loadSvgPainter(stream, density).also {
            ImageCache.putPainter(url, it)
        }
    }
}

suspend fun loadXmlImageVector(vm: MainViewModel, url: String, density: Density): ImageVector {
    ImageCache.getImageVector(url)?.let { return it }

    return urlStream(vm, url).use { stream ->
        loadXmlImageVector(InputSource(stream), density).also {
            ImageCache.putImageVector(url, it)
        }
    }
}

private suspend fun urlStream(vm: MainViewModel, url: String): ByteArrayInputStream {
    val response: HttpResponse = vm.client.get(url)
    val byteArray = response.readBytes()
    return ByteArrayInputStream(byteArray)
}
