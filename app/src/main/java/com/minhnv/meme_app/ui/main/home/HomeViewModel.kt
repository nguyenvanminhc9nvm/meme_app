package com.minhnv.meme_app.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.utils.TrackingError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataManager: AppDataManager,
    private val trackingErrorHelper: TrackingError
): ViewModel() {
    val communities = Pager(PagingConfig(10)) {
        CommunityDataSource("hot", "viral", "day", dataManager)
    }.flow.cachedIn(viewModelScope)
}