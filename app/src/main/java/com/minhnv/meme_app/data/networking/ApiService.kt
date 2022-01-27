package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.request.LoginRequest
import com.minhnv.meme_app.data.networking.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun doLogin(@Body loginRequest: LoginRequest): LoginResponse

    @Headers()
    suspend fun doGetList()
}