package com.minhnv.meme_app.ui.main.home.tags

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.data.networking.model.response.Community

class TagDataSource(
    private val tagName: String,
    private val sort: String,
    private val window: String,
    private val dataManager: AppDataManager
): PagingSource<Int, Community>() {
    override fun getRefreshKey(state: PagingState<Int, Community>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Community> {
        return try {
            val currentLoadingPageKey = params.key ?: 1
            val response = dataManager.doGetTagInfo(
                tagName,
                sort,
                currentLoadingPageKey,
                window
            )
            val responseData = mutableListOf<Community>()
            responseData.addAll(response.data?.items ?: mutableListOf())
            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}