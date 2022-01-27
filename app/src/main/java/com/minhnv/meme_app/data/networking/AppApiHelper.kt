package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.request.LoginRequest
import com.minhnv.meme_app.data.networking.model.response.LoginResponse
import javax.inject.Inject

class AppApiHelper @Inject constructor(
    private val apiService: ApiService
): ApiHelper {
    override suspend fun doLogin(loginRequest: LoginRequest): LoginResponse {
        return apiService.doLogin(loginRequest)
    }
}