package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.data.networking.model.request.LoginRequest
import com.minhnv.meme_app.data.networking.model.response.LoginResponse
import com.minhnv.meme_app.ui.main.create.meme_template.export.MemeIcon
import com.minhnv.meme_app.ui.main.create.meme_template.export.memeIcons
import com.minhnv.meme_app.ui.main.create.meme_template.memeTemplates
import javax.inject.Inject

class AppApiHelper @Inject constructor(
    private val apiService: ApiService
): ApiHelper {
    override suspend fun doLogin(loginRequest: LoginRequest): LoginResponse {
        return apiService.doLogin(loginRequest)
    }

    override suspend fun doFetchListMeme(): MutableList<MemeTemplate> {
        return memeTemplates
    }

    override suspend fun doFetchListMemeIcon(): MutableList<MemeIcon> {
        return memeIcons
    }
}