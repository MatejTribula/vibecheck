package dk.sdu.vibecheck.ui.theme

import dk.sdu.vibecheck.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CormorantGaramond = FontFamily(
    Font(R.font.cormorant_garamond, FontWeight.Normal),
    Font(R.font.cormorant_garamond, FontWeight.Medium),
    Font(R.font.cormorant_garamond, FontWeight.Bold)
)

val DmSans = FontFamily(
    Font(R.font.dm_sans, FontWeight.Normal),
    Font(R.font.dm_sans, FontWeight.Medium),
    Font(R.font.dm_sans, FontWeight.SemiBold),
    Font(R.font.dm_sans, FontWeight.Bold)
)

object VibeTextStyles {
    val Header = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontSize = 50.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp
    )

    val Subtitle = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )

    val ButtonLabel = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

    val JoinTitle = Header
    val JoinBody = Subtitle
    val JoinButton = ButtonLabel
}

val VibeTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
).copy(
    displayLarge = Typography().displayLarge.copy(fontFamily = DmSans),
    displayMedium = Typography().displayMedium.copy(fontFamily = DmSans),
    displaySmall = Typography().displaySmall.copy(fontFamily = DmSans),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = DmSans),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = DmSans),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = DmSans),
    titleLarge = Typography().titleLarge.copy(fontFamily = DmSans),
    titleMedium = Typography().titleMedium.copy(fontFamily = DmSans),
    titleSmall = Typography().titleSmall.copy(fontFamily = DmSans),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = DmSans),
    bodySmall = Typography().bodySmall.copy(fontFamily = DmSans),
    labelLarge = Typography().labelLarge.copy(fontFamily = DmSans),
    labelMedium = Typography().labelMedium.copy(fontFamily = DmSans),
    labelSmall = Typography().labelSmall.copy(fontFamily = DmSans)
)