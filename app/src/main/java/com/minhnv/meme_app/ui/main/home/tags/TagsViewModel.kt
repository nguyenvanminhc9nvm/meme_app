package com.minhnv.meme_app.ui.main.home.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.data.networking.model.response.Community
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val dataManager: AppDataManager
): ViewModel() {

    val tags: (String) -> Flow<PagingData<Community>> = {
        Pager(PagingConfig(10)) {
            TagDataSource(it, "viral", "day", dataManager)
        }.flow.cachedIn(viewModelScope)
    }
}