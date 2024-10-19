package com.rkm.tasky.util.storage.implementation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
        SESSION_ACCESS_TOKEN_KEY to stringPreferencesKey(SESSION_ACCESS_TOKEN_KEY),
        SESSION_REFRESH_TOKEN_KEY to stringPreferencesKey(SESSION_REFRESH_TOKEN_KEY),
        SESSION_USER_ID_KEY to stringPreferencesKey(SESSION_USER_ID_KEY),
        SESSION_FULL_NAME_KEY to stringPreferencesKey(SESSION_FULL_NAME_KEY),
        SESSION_EMAIL_KEY to stringPreferencesKey(SESSION_EMAIL_KEY)
    )

    private companion object {
        const val SESSION_ACCESS_TOKEN_KEY = "SESSION_ACCESS_TOKEN"
        const val SESSION_REFRESH_TOKEN_KEY = "SESSION_REFRESH_TOKEN"
        const val SESSION_USER_ID_KEY = "SESSION_USER_ID"
        const val SESSION_FULL_NAME_KEY = "SESSION_FULL_NAME"
        const val SESSION_EMAIL_KEY = "SESSION_EMAIL"
    }

    override suspend fun getSession(): AuthInfo? {

        val authInfo = dataStore.data.map { preferences ->
            val accessToken = preferences[keyMap[SESSION_ACCESS_TOKEN_KEY]!!]?.let { key ->
                encryptor.decrypt(key)
            } ?: ""
            val refreshToken = preferences[keyMap[SESSION_REFRESH_TOKEN_KEY]!!]?.let { key ->
                encryptor.decrypt(key)
            } ?: ""
            val userId = preferences[keyMap[SESSION_USER_ID_KEY]!!]?.let { key ->
                encryptor.decrypt(key)
            } ?: ""
            val name = preferences[keyMap[SESSION_FULL_NAME_KEY]!!]?.let { key ->
                encryptor.decrypt(key)
            } ?: ""
            val email = preferences[keyMap[SESSION_EMAIL_KEY]!!]?.let { key ->
                encryptor.decrypt(key)
            } ?: ""

            if (accessToken.isEmpty() && refreshToken.isEmpty() && userId.isEmpty()
                && name.isEmpty() && email.isEmpty()
            ) {
                null
            } else {
                AuthInfo(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    userId = userId,
                    fullName = name,
                    email = email
                )
            }
        }.firstOrNull()

        return authInfo
    }

    override suspend fun setSession(info: AuthInfo) {
        dataStore.edit { preferences ->
            if (info.accessToken.isNotEmpty()) preferences[keyMap[SESSION_ACCESS_TOKEN_KEY]!!] =
                encryptor.encrypt(info.accessToken)
            if (info.refreshToken.isNotEmpty()) preferences[keyMap[SESSION_REFRESH_TOKEN_KEY]!!] =
                encryptor.encrypt(info.refreshToken)
            if (info.fullName.isNotEmpty()) preferences[keyMap[SESSION_USER_ID_KEY]!!] =
                encryptor.encrypt(info.userId)
            if (info.userId.isNotEmpty()) preferences[keyMap[SESSION_FULL_NAME_KEY]!!] =
                encryptor.encrypt(info.fullName)
            if (info.email.isNotEmpty()) preferences[keyMap[SESSION_EMAIL_KEY]!!] =
                encryptor.encrypt(info.email)
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}