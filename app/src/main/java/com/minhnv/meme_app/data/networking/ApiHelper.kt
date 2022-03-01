package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.data.networking.model.response.*
import com.minhnv.meme_app.ui.main.create.meme_template.export.MemeIcon
import com.minhnv.meme_app.utils.helper.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ApiHelper {
    suspend fun doFetchListMeme(): MutableList<MemeTemplate>

    suspend fun doFetchListMemeIcon(): MutableList<MemeIcon>

    suspend fun doRefreshToken(accessTokenRequest: AccessTokenRequest): Flow<Resource<AccessTokenResponse>>

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

    suspend fun doGetListImages(
        page: Int,
        token: String
    ): ImagesResponse

    suspend fun doUploadFile(
        title: String?,
        description: String?,
        file: MultipartBody.Part,
        token: String
    ): Flow<Resource<UploadResponse>>
}