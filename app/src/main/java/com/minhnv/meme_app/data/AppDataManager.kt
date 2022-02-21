package com.minhnv.meme_app.data

import com.minhnv.meme_app.data.data_store.DataStoreHelper
import com.minhnv.meme_app.data.database.DbHelper
import com.minhnv.meme_app.data.database.entity.Communities
import com.minhnv.meme_app.data.networking.ApiHelper
import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppDataManager @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreHelper: DataStoreHelper,
    private val dbHelper: DbHelper
) {

    suspend fun themeSystem() = dataStoreHelper.themeSystem()

    suspend fun changeThemeSystem(isDark: Boolean) {
        dataStoreHelper.setThemeSystem(isDark)
    }

    suspend fun doGetListMemeTemplates() = apiHelper.doFetchListMeme()

    suspend fun doGetListMemeIcon() = apiHelper.doFetchListMemeIcon()

    suspend fun doRefreshToken(accessTokenRequest: AccessTokenRequest) =
        apiHelper.doRefreshToken(accessTokenRequest)

    suspend fun doSaveToken(accessToken: String) {
        dataStoreHelper.saveToken(accessToken)
    }

    suspend fun doGetListCommunity(
        section: String,
        sort: String,
        page: Int,
        window: String
    ) = withContext(Dispatchers.IO) {
        val response = apiHelper.doGetListCommunity(
            section,
            sort,
            page,
            window,
            Constants.Bearer + dataStoreHelper.token()
        )
        val communitiesResponse = response.data ?: mutableListOf()
        val communitiesLocal = dbHelper.communities()
        val communityNonConflictStrategy = communitiesResponse.filter { community ->
            communitiesLocal.all { it.idString != community.id }
        }
        communityNonConflictStrategy
    }

    suspend fun checkTokenIsNotEmpty(): Boolean {
        return dataStoreHelper.token().isNotEmpty()
    }

    suspend fun insertCommunities(communities: Communities) {
        dbHelper.insertCommunities(communities)
    }

    suspend fun communities() = dbHelper.communities()

    suspend fun doVotingPost(
        galleryHash: String,
        vote: String
    ) = apiHelper.doVotingPost(galleryHash, vote, Constants.Bearer + dataStoreHelper.token())

    suspend fun doGetTagInfo(
        tagName: String,
        sort: String,
        page: Int,
        window: String,
    ) = apiHelper.doGetTagInfo(tagName, sort, page, window, Constants.Bearer + dataStoreHelper.token())
}