package com.minhnv.meme_app.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.data.networking.model.request.AccessTokenRequest
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.helper.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataManager: AppDataManager
) : ViewModel() {
    private val _position: MutableLiveData<Int> = MutableLiveData()
    val position: LiveData<Int> get() = _position

    fun setPosition(newPos: Int) {
        _position.value = newPos
    }

    val communities = Pager(PagingConfig(10)) {
        CommunityDataSource("hot", "viral", "day", dataManager)
    }.flow.cachedIn(viewModelScope)

    fun didLoadCommunities(
        section: String,
        sort: String,
        window: String
    ) = Pager(PagingConfig(10)) {
            CommunityDataSource(section, sort, window, dataManager)
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
            dataManager.doRefreshToken(accessTokenRequest).collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.accessToken?.let { token -> dataManager.doSaveToken(token) }
                    }
                    else -> println("refresh Error")
                }
            }
        }
    }
}