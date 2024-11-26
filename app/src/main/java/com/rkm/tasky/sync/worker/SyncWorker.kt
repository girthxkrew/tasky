package com.rkm.tasky.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rkm.tasky.di.ApplicationCoroutineScope
import com.rkm.tasky.sync.manager.abstraction.SyncManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@HiltWorker
class SyncWorker @AssistedInject constructor(
    private val syncManager: SyncManager,
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters
): CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        scope.launch {
            syncManager.syncCreatedItems()
            syncManager.syncDeletedItems()
            syncManager.syncUpdatedItems()
        }
        return Result.success()
    }

    companion object {
        const val SYNC_WORKER_TAG = "SYNC_WORKER"
    }
}