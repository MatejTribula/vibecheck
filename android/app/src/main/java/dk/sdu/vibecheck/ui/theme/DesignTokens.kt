package dk.sdu.vibecheck.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Non-Material tokens shared by components. Scheme colors remain in [MaterialTheme]
 * so light/dark and contrast modes can be selected in one place.
 */
object VibeTokens {
    object Spacing {
        val Small = 8.dp
        val Medium = 12.dp
        val Large = 16.dp
        val ExtraLarge = 20.dp
        val ScreenHorizontal = 31.dp
    }

    object Shapes {
        val Button = 20.dp
        val Field = 12.dp
        val Card = 16.dp
        val ScanCard = 20.dp
    }

    object Colors {
        val JoinInput = JoinInputBackground
        val QrPlaceholder = QrPlaceholderBackground
        val LightSecondaryAction = PowderBlueContainer
        val LightOnSecondaryAction = OnPowderBlueContainer
        val DarkSecondaryAction = DarkPowderBlueContainer
        val DarkOnSecondaryAction = DarkOnPowderBlueContainer
    }
}
