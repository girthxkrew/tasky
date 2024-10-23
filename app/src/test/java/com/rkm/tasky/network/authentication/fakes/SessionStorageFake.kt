package com.rkm.tasky.network.authentication.fakes

import com.rkm.tasky.util.storage.abstraction.SessionStorage
import com.rkm.tasky.util.storage.model.AuthInfo

class SessionStorageFake: SessionStorage {
    private val storage = mutableListOf<AuthInfo>()

    override suspend fun getSession(): AuthInfo? {
        if(storage.isEmpty()) return null
        return storage[storage.lastIndex]
    }

    override suspend fun setSession(info: AuthInfo) {
        storage.add(info)
    }

    override suspend fun clearSession() {
        storage.clear()
    }
}