package ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconToggleButton(
    isSelected: Boolean,
    icon: ImageVector,
    text: String? = null,
    onIconSelected: () -> Unit
) {
    if (isSelected) {
        // Utilisez Button pour le bouton sélectionné
        Button(
            onClick = onIconSelected,
            modifier = PointerModifier.padding(horizontal = 2.dp)
        ) {
            ButtonContent(icon, text, isSelected)
        }
    } else {
        // Utilisez OutlinedButton pour les boutons non sélectionnés
        OutlinedButton(
            onClick = onIconSelected,
            modifier = PointerModifier.padding(horizontal = 2.dp)
        ) {
            ButtonContent(icon, text, isSelected)
        }
    }
}

@Composable
fun ButtonContent(
    icon: ImageVector,
    text: String?,
    isSelected: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(15.dp),
        )
        // Affiche le texte uniquement s'il n'est pas null
        text?.let {
            Text(
                it,
                modifier = Modifier.padding(start = 5.dp),
            )
        }
    }
}

