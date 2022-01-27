package com.minhnv.meme_app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minhnv.meme_app.data.database.dao.UserDao
import com.minhnv.meme_app.data.database.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract val userDao: UserDao
}
