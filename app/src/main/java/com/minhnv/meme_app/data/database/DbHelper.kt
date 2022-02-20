package com.minhnv.meme_app.data.database

import com.minhnv.meme_app.data.database.entity.Communities

interface DbHelper {
    suspend fun insertCommunities(mutableList: Communities)

    suspend fun communities(): MutableList<Communities>
}