package com.minhnv.meme_app.data.database

import com.minhnv.meme_app.data.database.entity.Communities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppDbHelper @Inject constructor(
    private val appDatabase: AppDatabase
): DbHelper {
    override suspend fun insertCommunities(communities: Communities) {
        return withContext(Dispatchers.IO) {
            appDatabase.communitiesDao.insertCommunities(communities)
        }
    }

    override suspend fun communities(): MutableList<Communities> {
        return withContext(Dispatchers.IO) {
            appDatabase.communitiesDao.getCommunities()
        }
    }
}