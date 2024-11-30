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
class OfflineAgendaSyncWorker @AssistedInject constructor(
    private val syncManager: SyncManager,
    @Assisted private val context: Context,
    @Assisted private val parameters: WorkerParameters,
): CoroutineWorker(context, parameters) {

    companion object {
        const val OFFLINE_AGENDA_SYNC_WORKER_TAG = "OFFLINE_AGENDA_SYNC_WORKER"
    }

    override suspend fun doWork(): Result  {

        val deleteSyncResults = syncManager.syncLocalDeletedAgendaItems()
        deleteSyncResults.onFailure { error ->
            return if (error == NetworkError.APIError.UPLOAD_FAILED) Result.failure()
            else Result.retry()
        }

        val createSyncResults = syncManager.syncLocalCreatedAgendaItems()
        createSyncResults.onFailure { error ->
            return if (error == NetworkError.APIError.UPLOAD_FAILED) Result.failure()
            else Result.retry()
        }

        val updateSyncResults = syncManager.syncLocalUpdatedAgendaItems()
        updateSyncResults.onFailure { error ->
            return if (error == NetworkError.APIError.UPLOAD_FAILED) Result.failure()
            else Result.retry()
        }

        return Result.success()
    }
}