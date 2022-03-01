package com.minhnv.meme_app.data.networking

import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.data.networking.model.response.*
import okhttp3.MultipartBody
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

    @GET("3/account/me/images")
    suspend fun doGetListImages(
        @Header("authorization") token: String
    ): ImagesResponse

    @Multipart
    @POST("3/image")
    suspend fun doUploadFile(
        @Query("title") title: String?,
        @Query("description") description: String?,
        @Part file: MultipartBody.Part,
        @Header("authorization") token: String
    ): UploadResponse
}
