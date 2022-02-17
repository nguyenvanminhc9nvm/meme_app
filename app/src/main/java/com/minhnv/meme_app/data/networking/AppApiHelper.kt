package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.data.networking.model.response.AccessTokenResponse
import com.minhnv.meme_app.data.networking.model.response.CommunityResponse
import com.minhnv.meme_app.ui.main.create.meme_template.export.MemeIcon
import com.minhnv.meme_app.ui.main.create.meme_template.export.memeIcons
import com.minhnv.meme_app.ui.main.create.meme_template.memeTemplates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppApiHelper @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun doFetchListMeme(): MutableList<MemeTemplate> {
        return memeTemplates
    }

    override suspend fun doFetchListMemeIcon(): MutableList<MemeIcon> {
        return memeIcons
    }

    override suspend fun doRefreshToken(accessTokenRequest: AccessTokenRequest): AccessTokenResponse {
        return withContext(Dispatchers.IO) {
            apiService.doRefreshToken(accessTokenRequest)
        }
    }

    override suspend fun doGetListCommunity(
        section: String,
        sort: String,
        page: Int,
        window: String,
        token: String
    ): CommunityResponse = withContext(Dispatchers.IO) {
        apiService.doGetListCommunity(
            section,
            sort,
            page,
            window,
            token
        )
    }
}