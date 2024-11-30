package com.rkm.tasky.network.authentication.implementation

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.rkm.tasky.di.IoDispatcher
import com.rkm.tasky.network.authentication.abstraction.AuthenticationManager
import com.rkm.tasky.network.authentication.mapper.asSessionInfo
import com.rkm.tasky.network.repository.abstraction.AuthenticationRepository
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.sync.worker.OfflineAgendaSyncWorker
import com.rkm.tasky.sync.worker.OfflineAgendaSyncWorker.Companion.OFFLINE_AGENDA_SYNC_WORKER_TAG
import com.rkm.tasky.sync.worker.RemoteAgendaSyncWorker
import com.rkm.tasky.sync.worker.RemoteAgendaSyncWorker.Companion.REMOTE_AGENDA_SYNC_WORKER_TAG
import com.rkm.tasky.util.result.EmptyResult
import com.rkm.tasky.util.result.asEmptyDataResult
import com.rkm.tasky.util.result.onSuccess
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    private val repository: AuthenticationRepository,
    private val sessionStorage: SessionStorage,
    private val workManager: WorkManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AuthenticationManager {
    override suspend fun logIn(
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> =
        withContext(dispatcher) {
            val result = repository.loginUser(email = email, password = password)
            result.onSuccess { user ->
                sessionStorage.setSession(user.asSessionInfo())
                scheduleWork()
            }
            return@withContext result.asEmptyDataResult()
        }

    override suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<NetworkError.APIError> = withContext(dispatcher) {
        return@withContext repository.registerUser(
            fullName = fullName,
            email = email,
            password = password
        ).asEmptyDataResult()
    }

    private fun scheduleWork() {

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true).build()

        val periodicOfflineSyncWorkRequest = PeriodicWorkRequestBuilder<OfflineAgendaSyncWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val periodicRemoteSyncWorkRequest = PeriodicWorkRequestBuilder<RemoteAgendaSyncWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            OFFLINE_AGENDA_SYNC_WORKER_TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicOfflineSyncWorkRequest
        )

        workManager.enqueueUniquePeriodicWork(
            REMOTE_AGENDA_SYNC_WORKER_TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicRemoteSyncWorkRequest
        )
    }
}