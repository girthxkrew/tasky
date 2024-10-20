package com.rkm.tasky.util.storage.abstraction

import com.rkm.tasky.util.storage.model.AuthInfo

interface SessionStorage {
    suspend fun getSession(): AuthInfo?
    suspend fun setSession(info: AuthInfo)
    suspend fun clearSession()
}