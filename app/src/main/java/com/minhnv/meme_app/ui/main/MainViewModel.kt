package com.minhnv.meme_app.ui.main

import androidx.lifecycle.ViewModel
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.utils.helper.TrackingError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataManager: AppDataManager,
    private val trackingErrorHelper: TrackingError
) : ViewModel() {
    init {
//        viewModelScope.launch(trackingErrorHelper.coroutineExceptionHandler()) {
//            val accessTokenRequest = AccessTokenRequest(
//                Constants.REFRESH_TOKEN,
//                Constants.CLIENT_ID,
//                Constants.CLIENT_SECRET,
//                "refresh_token"
//            )
//            dataManager.doRefreshToken(accessTokenRequest).accessToken?.let {
//                dataManager.doSaveToken(it)
//            }
//        }
    }
}
