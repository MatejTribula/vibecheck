package dk.sdu.vibecheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dk.sdu.vibecheck.ui.navigation.AppNavigation
import dk.sdu.vibecheck.ui.theme.VibecheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VibecheckTheme {
                AppNavigation()
            }
        }
    }
}
