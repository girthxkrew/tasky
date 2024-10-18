package com.rkm.tasky.util.storage.preferences.implementation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rkm.tasky.util.storage.preferences.abstraction.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
   private val dataStore: DataStore<Preferences>
): UserPreferencesRepository {

    private val keyMap = mapOf(
        USER_KEY_ACCESS_TOKEN to stringPreferencesKey(USER_KEY_ACCESS_TOKEN),
        USER_KEY_RESPONSE_TOKEN to stringPreferencesKey(USER_KEY_RESPONSE_TOKEN)
    )

    override suspend fun saveValue(key: String, value: String) {
        dataStore.edit { preferences ->
           preferences[keyMap[key]!!] = value
        }
    }

    val accessTokenFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[keyMap[USER_KEY_ACCESS_TOKEN]!!] ?: ""
    }

    val responseTokenFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[keyMap[USER_KEY_RESPONSE_TOKEN]!!] ?: ""
    }

    companion object {
        const val USER_KEY_ACCESS_TOKEN = "USER_ACCESS_TOKEN"
        const val USER_KEY_RESPONSE_TOKEN = "USER_RESPONSE_TOKEN"
    }
}