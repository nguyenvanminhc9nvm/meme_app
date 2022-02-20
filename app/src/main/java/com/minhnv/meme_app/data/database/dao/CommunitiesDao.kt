package com.minhnv.meme_app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.minhnv.meme_app.data.database.entity.Communities

@Dao
interface CommunitiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunities(communities: Communities)

    @Query("SELECT * FROM TB_COMMUNITIES")
    suspend fun getCommunities(): MutableList<Communities>
}