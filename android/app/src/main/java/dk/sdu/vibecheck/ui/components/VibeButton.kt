package dk.sdu.vibecheck.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import dk.sdu.vibecheck.ui.theme.VibeTokens

enum class VibeButtonVariant {
    Primary,
    Secondary,
    Translucent
}

@Composable
fun VibeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: VibeButtonVariant = VibeButtonVariant.Primary
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = when (variant) {
        VibeButtonVariant.Primary -> MaterialTheme.colorScheme.primary
        VibeButtonVariant.Secondary -> if (isDarkTheme) {
            VibeTokens.Colors.DarkSecondaryAction
        } else {
            VibeTokens.Colors.LightSecondaryAction
        }
        VibeButtonVariant.Translucent -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
    }

    val textColor = when (variant) {
        VibeButtonVariant.Primary -> MaterialTheme.colorScheme.onPrimary
        VibeButtonVariant.Secondary -> if (isDarkTheme) {
            VibeTokens.Colors.DarkOnSecondaryAction
        } else {
            VibeTokens.Colors.LightOnSecondaryAction
        }
        VibeButtonVariant.Translucent -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(VibeTokens.Shapes.Button))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = VibeTokens.Spacing.ExtraLarge,
                vertical = VibeTokens.Spacing.Medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
