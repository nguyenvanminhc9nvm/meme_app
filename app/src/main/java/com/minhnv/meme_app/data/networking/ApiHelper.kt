package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.data.networking.model.response.AccessTokenResponse
import com.minhnv.meme_app.data.networking.model.response.BasicResponse
import com.minhnv.meme_app.data.networking.model.response.CommunityResponse
import com.minhnv.meme_app.data.networking.model.response.TagsResponse
import com.minhnv.meme_app.ui.main.create.meme_template.export.MemeIcon

interface ApiHelper {
    suspend fun doFetchListMeme(): MutableList<MemeTemplate>

    suspend fun doFetchListMemeIcon(): MutableList<MemeIcon>

    suspend fun doRefreshToken(accessTokenRequest: AccessTokenRequest): AccessTokenResponse

    suspend fun doGetListCommunity(
        section: String,
        sort: String,
        page: Int,
        window: String,
        token: String
    ): CommunityResponse

    suspend fun doVotingPost(
        galleryHash: String,
        vote: String,
        token: String
    ): BasicResponse

    suspend fun doGetTagInfo(
        tagName: String,
        sort: String,
        page: Int,
        window: String,
        token: String
    ): TagsResponse
}