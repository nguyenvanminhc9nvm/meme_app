package com.minhnv.meme_app.ui.main.deep

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.data.networking.model.response.Images

class DeepDataSource(
    private val dataManager: AppDataManager
): PagingSource<Int, Images>() {
    override fun getRefreshKey(state: PagingState<Int, Images>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Images> {
        return try {
            val currentLoadingPageKey = params.key ?: 1
            val response = dataManager.doGetListImages(
                currentLoadingPageKey
            )
            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            val responseData = mutableListOf<Images>()
            responseData.addAll(response.data ?: mutableListOf())
            val nextKey = if (response.data.isNullOrEmpty()) {
                null
            } else {
                currentLoadingPageKey.plus(1)
            }
            LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}