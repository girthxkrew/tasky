package com.rkm.tasky.util.storage.abstraction

import com.rkm.tasky.util.storage.model.SessionInfo

interface SessionStorage {
    suspend fun getSession(): SessionInfo?
    suspend fun setSession(info: SessionInfo)
    suspend fun clearSession()
}