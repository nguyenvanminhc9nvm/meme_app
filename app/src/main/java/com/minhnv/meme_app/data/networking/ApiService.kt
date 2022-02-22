package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.data.networking.model.response.*
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
        @Header("authorization") token: String
    ): CommunityResponse

    @POST("3/gallery/{galleryHash}/vote/{vote}")
    suspend fun doVotingPost(
        @Path("galleryHash") galleryHash: String,
        @Path("vote") vote: String,
        @Header("authorization") token: String
    ): BasicResponse

    @GET("3/gallery/t/{tagName}/{sort}/{window}/{page}")
    suspend fun doGetTagsInfo(
        @Path("tagName") tagName: String,
        @Path("sort") sort: String,
        @Path("page") page: Int,
        @Path("window") window: String,
        @Header("authorization") token: String
    ): TagsResponse

    @GET("3/account/c9xnvm/images/{page}")
    suspend fun doGetListImages(
        @Path("page") page: Int,
        @Header("authorization") token: String
    ): ImagesResponse

    @POST("3/upload")
    suspend fun doUploadFile(): UploadResponse
}
