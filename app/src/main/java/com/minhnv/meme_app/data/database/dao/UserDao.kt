package com.minhnv.meme_app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.minhnv.meme_app.data.database.entity.User
import com.minhnv.meme_app.utils.Constants

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM ${Constants.TB_USER}")
    suspend fun getUser(): MutableList<User>
}
