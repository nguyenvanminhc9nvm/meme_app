package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.data.networking.model.request.LoginRequest
import com.minhnv.meme_app.data.networking.model.response.LoginResponse
import com.minhnv.meme_app.ui.main.create.meme_template.export.MemeIcon

interface ApiHelper {
    suspend fun doLogin(loginRequest: LoginRequest): LoginResponse

    suspend fun doFetchListMeme(): MutableList<MemeTemplate>

    suspend fun doFetchListMemeIcon(): MutableList<MemeIcon>
}