package com.rkm.tasky.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rkm.tasky.di.ApplicationCoroutineScope
import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.sync.manager.abstraction.SyncManager
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.util.result.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

@HiltWorker
class DeleteSyncWorker @AssistedInject constructor(
    private val syncManager: SyncManager,
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result = withContext(scope.coroutineContext) {
        val result = syncManager.syncDeletedItems()
        result.onSuccess {
            return@withContext Result.success()
        }
        return@withContext if ((result as com.rkm.tasky.util.result.Result.Error).error
            == NetworkError.APIError.UPLOAD_FAILED) Result.failure()
        else Result.retry()
    }

    companion object {
        const val DELETE_SYNC_WORKER_TAG = "DELETE_SYNC_WORKER"
    }
}