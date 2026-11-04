package dk.sdu.vibecheck

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dk.sdu.vibecheck.ui.navigation.AppNavigation
import dk.sdu.vibecheck.ui.theme.VibecheckTheme
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket

class MainActivity : ComponentActivity() {

    private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val options = IO.Options().apply {
                transports = arrayOf(WebSocket.NAME)
                reconnection = true
            }

//            you have to set IP
            socket = IO.socket("http://10.126.65.210:3000", options)

            socket.on(Socket.EVENT_CONNECT) {
                Log.d("Socket", "CONNECTED successfully! ID: ${socket.id()}")
            }

            socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                val err = args.firstOrNull()
                if (err is Throwable) {
                    Log.e("Socket", "CONNECT ERROR: ${err.message}", err)
                } else {
                    Log.e("Socket", "CONNECT ERROR: $err")
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("Socket", "DISCONNECTED")
            }

            socket.connect()
        } catch (e: Exception) {
            Log.e("Socket", "Failed to setup socket", e)
        }

        enableEdgeToEdge()
        setContent {
            VibecheckTheme {
                AppNavigation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::socket.isInitialized) {
            socket.disconnect()
            socket.off()
        }
    }
}
