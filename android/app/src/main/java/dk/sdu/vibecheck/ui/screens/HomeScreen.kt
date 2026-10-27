package dk.sdu.vibecheck.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dk.sdu.vibecheck.ui.navigation.Screen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToScreen: (Screen) -> Unit = {}
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Navigate to views:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigateToScreen(Screen.GALLERY) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Component Gallery")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onNavigateToScreen(Screen.JOIN_SESSION) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Join Session")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onNavigateToScreen(Screen.WAITING_ROOM) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Waiting Room")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onNavigateToScreen(Screen.RATING_PHASE) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Rating Phase")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onNavigateToScreen(Screen.COMPATIBILITY_RESULT) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compatibility Result")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onNavigateToScreen(Screen.PLAYLIST_PREVIEW) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Playlist Preview")
            }
        }
    }
}
