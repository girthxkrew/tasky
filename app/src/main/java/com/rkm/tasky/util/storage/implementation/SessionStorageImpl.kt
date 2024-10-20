package com.rkm.tasky.util.storage.implementation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.rkm.tasky.util.security.implementation.DataStoreEncryptor
import com.rkm.tasky.util.storage.abstraction.SessionStorage
import com.rkm.tasky.util.storage.model.AuthInfo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val encryptor: DataStoreEncryptor
) : SessionStorage {

    private val keyMap = mapOf(
        SESSION_AUTH_USER_KEY to stringPreferencesKey(SESSION_AUTH_USER_KEY)
    )

    private companion object {
        const val SESSION_AUTH_USER_KEY = "SESSION_AUTH_USER"
    }

    override suspend fun getSession(): AuthInfo? {

        val authInfo = dataStore.data.map { preferences ->
            val user = preferences[keyMap[SESSION_AUTH_USER_KEY]!!]?.let { key ->
                encryptor.decrypt(key)
            } ?: ""
            if(user.isEmpty()) {
                null
            } else {
                authUserFromString(user)
            }
        }.firstOrNull()

        return authInfo
    }

    override suspend fun setSession(info: AuthInfo) {
        dataStore.edit { preferences ->
            preferences[keyMap[SESSION_AUTH_USER_KEY]!!] = encryptor.encrypt(authUserToString(info))
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(keyMap[SESSION_AUTH_USER_KEY]!!)
        }
    }

    private fun authUserFromString(user: String): AuthInfo {
        return Gson().fromJson(user, AuthInfo::class.java)
    }

    private fun authUserToString(user: AuthInfo): String {
        return Gson().toJson(user)
    }
}