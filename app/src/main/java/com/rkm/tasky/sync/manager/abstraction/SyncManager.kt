package com.rkm.tasky.sync.manager.abstraction

import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult

interface SyncManager {
    suspend fun syncDeletedItems(): EmptyResult<NetworkError.APIError>
    suspend fun syncUpdatedItem(): EmptyResult<NetworkError.APIError>
    suspend fun syncCreatedItems(): EmptyResult<NetworkError.APIError>
}