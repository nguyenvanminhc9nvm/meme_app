package com.minhnv.meme_app.data.networking

import com.google.gson.Gson
import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.data.networking.model.response.BasicResponse
import com.minhnv.meme_app.data.networking.model.response.CommunityResponse
import com.minhnv.meme_app.data.networking.model.response.ErrorResponse
import com.minhnv.meme_app.data.networking.model.response.UploadResponse
import com.minhnv.meme_app.ui.main.create.meme_template.export.MemeIcon
import com.minhnv.meme_app.ui.main.create.meme_template.export.memeIcons
import com.minhnv.meme_app.ui.main.create.meme_template.memeTemplates
import com.minhnv.meme_app.utils.helper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.HttpException
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

    override suspend fun doRefreshToken(
        accessTokenRequest: AccessTokenRequest
    ) = flow {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiService.doRefreshToken(accessTokenRequest)))
        } catch (e: Exception) {
            emit(errorMes(e))
        }
    }.flowOn(Dispatchers.IO)

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

    override suspend fun doVotingPost(
        galleryHash: String,
        vote: String,
        token: String
    ): BasicResponse {
        return withContext(Dispatchers.IO) {
            apiService.doVotingPost(galleryHash, vote, token)
        }
    }

    override suspend fun doGetTagInfo(
        tagName: String,
        sort: String,
        page: Int,
        window: String,
        token: String
    ) = withContext(Dispatchers.IO) {
        apiService.doGetTagsInfo(tagName, sort, page, window, token)
    }

    override suspend fun doGetListImages(
        page: Int,
        token: String
    ) = withContext(Dispatchers.IO) {
        apiService.doGetListImages(token)
    }

    override suspend fun doUploadFile(
        title: String?,
        description: String?,
        file: MultipartBody.Part,
        token: String
    ): Flow<Resource<UploadResponse>> {
        return flow {
            emit(Resource.loading(data = null))
            try {
                emit(
                    Resource.success(
                        data = apiService.doUploadFile(
                            title,
                            description,
                            file,
                            token
                        )
                    )
                )
            } catch (exception: Exception) {
                emit(errorMes(exception))
            }
        }
    }

    private fun errorMes(exception: Exception) = when (exception) {
        is HttpException -> {
            val gson = Gson()
            val errorBody = exception.response()?.errorBody()?.string()
            val response = gson.fromJson(errorBody, ErrorResponse::class.java)
            Resource.error(
                data = null,
                message = response.data?.data ?: "Error Occurred!"
            )
        }
        else -> Resource.error(
            data = null,
            message = exception.message ?: "Error Occurred!"
        )
    }
}