package com.rkm.tasky.network.fakes

import com.rkm.tasky.util.storage.abstraction.SessionStorage
import com.rkm.tasky.util.storage.model.SessionInfo

class SessionStorageFake: SessionStorage {
    private val storage = mutableListOf<SessionInfo>()

    override suspend fun getSession(): SessionInfo? {
        if(storage.isEmpty()) return null
        return storage[storage.lastIndex]
    }

    override suspend fun setSession(info: SessionInfo) {
        storage.add(info)
    }

    override suspend fun clearSession() {
        storage.clear()
    }
}