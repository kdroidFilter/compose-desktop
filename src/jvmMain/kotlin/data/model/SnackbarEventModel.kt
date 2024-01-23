package data.model

import androidx.compose.material3.SnackbarDuration

data class SnackbarEvent(
    val message: String,
    val duration: SnackbarDuration,
    val actionLabel: String?,
    val action: () -> Unit,
    val withDismissAction : Boolean,
    val dismissAction : () -> Unit
)
