package com.minhnv.meme_app.ui.main.deep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.minhnv.meme_app.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeepViewModel @Inject constructor(
    private val dataManager: AppDataManager
): ViewModel() {
    val deeps = Pager(PagingConfig(10)) {
        DeepDataSource(dataManager = dataManager)
    }.flow.cachedIn(viewModelScope)
}