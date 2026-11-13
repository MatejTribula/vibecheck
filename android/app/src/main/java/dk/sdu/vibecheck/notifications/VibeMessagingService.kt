package dk.sdu.vibecheck.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dk.sdu.vibecheck.MainActivity
import dk.sdu.vibecheck.R

class VibeMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        PushTokenSync.enqueue(applicationContext)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val payload = message.notification ?: return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        createChannel(this)
        val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val openApp = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_vibe_notification)
            .setContentTitle(payload.title ?: getString(R.string.app_name))
            .setContentText(payload.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(payload.body))
            .setContentIntent(openApp)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(message.messageId, 0, notification)
    }

    companion object {
        private const val channelId = "vibe_updates"

        fun createChannel(context: Context) {
            val channel = NotificationChannel(channelId, "Vibe Check updates", NotificationManager.IMPORTANCE_DEFAULT)
            context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }
}
