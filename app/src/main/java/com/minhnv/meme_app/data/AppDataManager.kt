package com.minhnv.meme_app.data

import com.minhnv.meme_app.data.data_store.DataStoreHelper
import com.minhnv.meme_app.data.database.DbHelper
import com.minhnv.meme_app.data.networking.ApiHelper
import com.minhnv.meme_app.data.networking.model.request.LoginRequest
import com.minhnv.meme_app.data.networking.model.response.LoginResponse
import javax.inject.Inject

class AppDataManager @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreHelper: DataStoreHelper,
    private val dbHelper: DbHelper
) {
    suspend fun doLogin(loginRequest: LoginRequest): LoginResponse {
        return apiHelper.doLogin(loginRequest)
    }

    suspend fun doSaveToken(token: String) {
        return dataStoreHelper.saveToken(token)
    }

    suspend fun themeSystem() = dataStoreHelper.themeSystem()

    suspend fun changeThemeSystem(isDark: Boolean) {
        dataStoreHelper.setThemeSystem(isDark)
    }
}