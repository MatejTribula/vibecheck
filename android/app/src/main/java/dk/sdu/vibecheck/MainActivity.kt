package dk.sdu.vibecheck

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import dk.sdu.vibecheck.data.local.EncryptedTokenStorage
import dk.sdu.vibecheck.data.remote.ApiClient
import dk.sdu.vibecheck.data.repositories.AuthRepositoryImpl
import dk.sdu.vibecheck.data.repositories.SessionRepositoryImpl
import dk.sdu.vibecheck.notifications.PushTokenSync
import dk.sdu.vibecheck.notifications.VibeMessagingService
import dk.sdu.vibecheck.ui.auth.AuthFlow
import dk.sdu.vibecheck.ui.auth.AuthViewModel
import dk.sdu.vibecheck.ui.navigation.AppNavigation
import dk.sdu.vibecheck.ui.theme.VibecheckTheme

class MainActivity : ComponentActivity() {
    private val requestNotificationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        VibeMessagingService.createChannel(this)

        val client = ApiClient(getString(R.string.api_base_url))
        val storage = EncryptedTokenStorage(applicationContext)
        val factory = AuthViewModel.factory(AuthRepositoryImpl(client, storage), SessionRepositoryImpl(client, storage))
        val auth = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setContent {
            VibecheckTheme {
                AuthFlow(auth) { state ->
                    LaunchedEffect(state.userId) {
                        enablePushNotifications()
                    }

                    AppNavigation(onLogout = auth::logout, isLoggingOut = state.isLoading, logoutError = state.errorMessage)
                }
            }
        }
    }

    private fun enablePushNotifications() {
        if (FirebaseApp.getApps(this).isEmpty()) {
            return
        }

        PushTokenSync.enqueue(applicationContext)

        val preferences = getSharedPreferences("notification_settings", MODE_PRIVATE)
        if (!preferences.getBoolean("permission_requested", false) &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            preferences.edit().putBoolean("permission_requested", true).apply()
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
