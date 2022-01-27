package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.request.LoginRequest
import com.minhnv.meme_app.data.networking.model.response.LoginResponse

interface ApiHelper {
    suspend fun doLogin(loginRequest: LoginRequest): LoginResponse
}