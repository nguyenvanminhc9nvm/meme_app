package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.data.networking.model.response.AccessTokenResponse
import com.minhnv.meme_app.data.networking.model.response.CommunityResponse
import retrofit2.http.*

interface ApiService {
    @Headers()
    suspend fun doGetList()

    @POST("oauth2/token")
    suspend fun doRefreshToken(
        @Body accessTokenRequest: AccessTokenRequest
    ): AccessTokenResponse

    @GET("3/gallery/{section}/{sort}/{window}/{page}")
    suspend fun doGetListCommunity(
        @Path("section") section: String,
        @Path("sort") sort: String,
        @Path("page") page: Int,
        @Path("window") window: String,
        @Header("Authorization") token: String
    ): CommunityResponse
}