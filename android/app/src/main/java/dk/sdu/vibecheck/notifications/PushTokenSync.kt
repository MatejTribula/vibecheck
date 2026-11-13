package dk.sdu.vibecheck.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dk.sdu.vibecheck.R
import dk.sdu.vibecheck.data.local.EncryptedTokenStorage
import dk.sdu.vibecheck.data.local.storageRequest
import dk.sdu.vibecheck.data.remote.ApiClient
import dk.sdu.vibecheck.domain.models.ErrorCode
import dk.sdu.vibecheck.domain.models.Result as ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class PushTokenSync(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (FirebaseApp.getApps(applicationContext).isEmpty()) {
            return Result.success()
        }

        val storage = EncryptedTokenStorage(applicationContext)
        val result = try {
            storageRequest<Unit> {
                val sessionToken = storage.getToken() ?: return@storageRequest ApiResult()
                val firebaseToken = withContext(Dispatchers.IO) {
                    Tasks.await(FirebaseMessaging.getInstance().token, 20, TimeUnit.SECONDS)
                }

                val client = ApiClient(applicationContext.getString(R.string.api_base_url))
                client.request(allowEmptyValue = true) {
                    client.api.updateFirebaseRegistration("Bearer $sessionToken", mapOf("firebaseRegistrationId" to firebaseToken))
                }
            }
        } catch (error: ExecutionException) {
            return retrySync()
        } catch (error: TimeoutException) {
            return retrySync()
        }

        return when (result.errorCode) {
            ErrorCode.None, ErrorCode.Unauthorized -> Result.success()
            ErrorCode.NetworkError, ErrorCode.InternalError, ErrorCode.StorageError -> retrySync()
            else -> Result.failure()
        }
    }

    private fun retrySync(): Result {
        return if (runAttemptCount < 4) Result.retry() else Result.failure()
    }

    companion object {
        fun enqueue(context: Context) {
            if (FirebaseApp.getApps(context).isEmpty()) {
                return
            }

            val request = OneTimeWorkRequestBuilder<PushTokenSync>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork("vibecheck-push-token", ExistingWorkPolicy.APPEND_OR_REPLACE, request)
        }
    }
}
