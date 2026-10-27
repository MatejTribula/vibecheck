package dk.sdu.vibecheck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dk.sdu.vibecheck.ui.theme.VibeTextStyles
import dk.sdu.vibecheck.ui.theme.VibeTokens

@Composable
fun JoinSessionScreen(
    modifier: Modifier = Modifier,
    onJoinWithCode: (String) -> Unit = {},
    onScanQrCode: () -> Unit = {}
) {
    var sessionCode by remember { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = VibeTokens.Spacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = VibeTokens.Spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Join a Session",
                    style = VibeTextStyles.Header,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Scan a QR code or enter a code\nto join a shared listening session.",
                    style = VibeTextStyles.Subtitle,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = VibeTokens.Spacing.Medium)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(VibeTokens.Shapes.ScanCard))
                    .background(colors.primary)
                    .padding(horizontal = 25.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QrPlaceholder(
                    onClick = onScanQrCode,
                    modifier = Modifier.size(110.dp)
                )

                Text(
                    text = "Scan a QR code",
                    style = VibeTextStyles.JoinBody,
                    color = colors.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = VibeTokens.Spacing.Small)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.onPrimary
                    )
                    Text(
                        text = "or",
                        style = VibeTextStyles.JoinBody,
                        color = colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.onPrimary
                    )
                }

                OutlinedTextField(
                    value = sessionCode,
                    onValueChange = { sessionCode = it.uppercase() },
                    placeholder = {
                        Text(
                            text = "Enter code",
                            style = VibeTextStyles.JoinButton,
                            color = colors.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        Text(
                            text = "7XQ2",
                            style = VibeTextStyles.JoinButton,
                            color = colors.onSurface
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VibeTokens.Colors.JoinInput,
                        unfocusedContainerColor = VibeTokens.Colors.JoinInput,
                        focusedBorderColor = VibeTokens.Colors.JoinInput,
                        unfocusedBorderColor = VibeTokens.Colors.JoinInput
                    ),
                    textStyle = VibeTextStyles.JoinButton
                )

                Button(
                    onClick = { onJoinWithCode(sessionCode) },
                    enabled = sessionCode.isNotBlank(),
                    shape = RoundedCornerShape(VibeTokens.Shapes.Button),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 21.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.onPrimaryContainer,
                        contentColor = colors.onPrimary
                    )
                ) {
                    Text(text = "Join", style = VibeTextStyles.JoinButton)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun QrPlaceholder(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(VibeTokens.Colors.QrPlaceholder)
            .clickable(onClick = onClick)
    )
}
