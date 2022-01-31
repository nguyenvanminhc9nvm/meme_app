package com.minhnv.meme_app.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore : DataStore<Preferences> by preferencesDataStore("preferencesKey")
interface DataStoreHelper {
    suspend fun saveToken(token: String)

    suspend fun token(): String

    suspend fun themeSystem(): Boolean

    suspend fun setThemeSystem(isDark: Boolean)
}
