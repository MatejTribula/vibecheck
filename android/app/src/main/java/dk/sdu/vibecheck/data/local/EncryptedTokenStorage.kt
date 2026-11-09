package dk.sdu.vibecheck.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.AtomicFile
import dk.sdu.vibecheck.domain.repositories.TokenStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.security.KeyStore
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class EncryptedTokenStorage(context: Context) : TokenStorage {
    private val file = AtomicFile(File(context.applicationContext.noBackupFilesDir, "session.token"))
    private val keyStore by lazy { KeyStore.getInstance("AndroidKeyStore").apply { load(null) } }

    override suspend fun saveToken(token: String): Unit = withContext(Dispatchers.IO) {
        mutex.withLock {
            require(token.isNotBlank())

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
            val encrypted = cipher.iv + cipher.doFinal(token.toByteArray(Charsets.UTF_8))

            val output = file.startWrite()
            try {
                output.write(encrypted)
                file.finishWrite(output)
            } catch (error: Exception) {
                file.failWrite(output)

                throw error
            }
        }
    }

    override suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        mutex.withLock {
            readToken()
        }
    }

    override suspend fun clearToken(expectedToken: String?): Unit = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (expectedToken == null || readToken() == expectedToken) {
                file.delete()
            }
        }
    }

    private fun readToken(): String? {
        val encrypted = try {
            file.readFully()
        } catch (error: FileNotFoundException) {
            return null
        }

        val key = keyStore.getKey(keyAlias, null) as? SecretKey
        if (key == null || encrypted.size <= 28) {
            file.delete()

            return null
        }

        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, encrypted.copyOfRange(0, 12)))

            return String(cipher.doFinal(encrypted.copyOfRange(12, encrypted.size)), Charsets.UTF_8)
        } catch (error: AEADBadTagException) {
            file.delete()

            return null
        }
    }

    private fun getOrCreateKey(): SecretKey {
        val key = keyStore.getKey(keyAlias, null) as? SecretKey
        if (key != null) {
            return key
        }

        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val specification = KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setKeySize(256)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        generator.init(specification)

        return generator.generateKey()
    }

    companion object {
        private const val keyAlias = "vibecheck.session.token"
        private val mutex = Mutex()
    }
}
