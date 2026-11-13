package dk.sdu.vibecheck.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dk.sdu.vibecheck.ui.screens.CompatibilityResultScreen
import dk.sdu.vibecheck.ui.screens.GalleryScreen
import dk.sdu.vibecheck.ui.screens.HomeScreen
import dk.sdu.vibecheck.ui.screens.JoinSessionScreen
import dk.sdu.vibecheck.ui.screens.PlaylistPreviewScreen
import dk.sdu.vibecheck.ui.screens.RatingPhaseScreen
import dk.sdu.vibecheck.ui.screens.WaitingRoomScreen

enum class Screen(val title: String) {
    HOME("Home"),
    JOIN_SESSION("Join Session"),
    WAITING_ROOM("Waiting Room"),
    RATING_PHASE("Rating Phase"),
    COMPATIBILITY_RESULT("Compatibility Result"),
    PLAYLIST_PREVIEW("Playlist Preview"),
    GALLERY("Component Gallery")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier, onLogout: (() -> Unit)? = null, isLoggingOut: Boolean = false, logoutError: String? = null) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (currentScreen == Screen.HOME && onLogout != null) {
                TopAppBar(
                    title = { Text("Vibe Check") },
                    actions = {
                        TextButton(onClick = onLogout, enabled = !isLoggingOut) {
                            Text(if (isLoggingOut) "Signing out…" else "Sign out")
                        }
                    }
                )
            } else if (currentScreen != Screen.HOME) {
                TopAppBar(
                    title = { Text(currentScreen.title) },
                    navigationIcon = {
                        TextButton(onClick = { currentScreen = Screen.HOME }) {
                            Text("← Back")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (logoutError != null) {
                Text(logoutError, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            when (currentScreen) {
                Screen.HOME -> HomeScreen(onNavigateToScreen = { currentScreen = it })
                Screen.JOIN_SESSION -> JoinSessionScreen()
                Screen.WAITING_ROOM -> WaitingRoomScreen()
                Screen.RATING_PHASE -> RatingPhaseScreen()
                Screen.COMPATIBILITY_RESULT -> CompatibilityResultScreen()
                Screen.PLAYLIST_PREVIEW -> PlaylistPreviewScreen()
                Screen.GALLERY -> GalleryScreen()
            }
        }
    }
}
