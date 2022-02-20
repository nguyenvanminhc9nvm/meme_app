package com.minhnv.meme_app.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.data.database.entity.Communities
import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.TrackingError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataManager: AppDataManager,
    private val trackingErrorHelper: TrackingError
) : ViewModel() {
    val communities = Pager(PagingConfig(10)) {
        CommunityDataSource("hot", "viral", "day", dataManager)
    }.flow.cachedIn(viewModelScope)

    suspend fun doGetAccessToken() {
        val accessTokenNotEmpty = dataManager.checkTokenIsNotEmpty()
        if (!accessTokenNotEmpty) {
            val accessTokenRequest = AccessTokenRequest(
                Constants.REFRESH_TOKEN,
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET,
                "refresh_token"
            )
            dataManager.doRefreshToken(accessTokenRequest).accessToken?.let {
                dataManager.doSaveToken(it)
            }
        }
    }

    fun saveListReadByUser(id: String) {
        viewModelScope.launch(trackingErrorHelper.coroutineExceptionHandler()) {
            dataManager.insertCommunities(Communities(null, id))
            println("#data: ${dataManager.communities()}")
        }
    }
}