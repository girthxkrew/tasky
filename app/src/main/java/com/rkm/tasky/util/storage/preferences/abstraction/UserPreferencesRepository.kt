package com.rkm.tasky.util.storage.preferences.abstraction

interface UserPreferencesRepository {
    suspend fun saveValue(key: String, value: String)
}