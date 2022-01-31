package com.minhnv.meme_app.data.data_store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDataStoreHelper @Inject constructor(
    private val context: Context
): DataStoreHelper {

    companion object {
        val KEY_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val KEY_THEME_SYSTEM = booleanPreferencesKey("KEY_THEME_SYSTEM")
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[KEY_TOKEN] = token
        }
    }

    override suspend fun token(): String {
        return context.dataStore.data.map {
            it[KEY_TOKEN]
        }.first() ?: ""
    }

    override suspend fun themeSystem(): Boolean {
        return context.dataStore.data.map {
            it[KEY_THEME_SYSTEM]
        }.first() ?: false
    }

    override suspend fun setThemeSystem(isDark: Boolean) {
        context.dataStore.edit {
            it[KEY_THEME_SYSTEM] = isDark
        }
    }
}
