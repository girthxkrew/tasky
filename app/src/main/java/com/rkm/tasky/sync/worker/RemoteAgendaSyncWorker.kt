package com.rkm.tasky.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.sync.manager.abstraction.SyncManager
import com.rkm.tasky.util.result.onFailure
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RemoteAgendaSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncManager: SyncManager
): CoroutineWorker(context, params) {

    companion object {
        const val REMOTE_AGENDA_SYNC_WORKER_TAG = "REMOTE_AGENDA_SYNC_WORKER"
    }

    override suspend fun doWork(): Result {
        val result = syncManager.syncRemoteAgendaItems()

        result.onFailure { error ->
            return if (error == NetworkError.APIError.UPLOAD_FAILED) Result.failure()
            else Result.retry()
        }

        return Result.success()
    }
}