package com.rkm.tasky.sync.manager.abstraction

import com.rkm.tasky.network.util.NetworkError
import com.rkm.tasky.util.result.EmptyResult

interface SyncManager {
    suspend fun syncLocalDeletedAgendaItems(): EmptyResult<NetworkError.APIError>
    suspend fun syncLocalUpdatedAgendaItems(): EmptyResult<NetworkError.APIError>
    suspend fun syncLocalCreatedAgendaItems(): EmptyResult<NetworkError.APIError>
    suspend fun syncRemoteAgendaItems(): EmptyResult<NetworkError.APIError>
}