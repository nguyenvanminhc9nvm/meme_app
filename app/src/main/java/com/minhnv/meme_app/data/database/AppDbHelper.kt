package com.minhnv.meme_app.data.database

import javax.inject.Inject

class AppDbHelper @Inject constructor(
    private val appDatabase: AppDatabase
): DbHelper {
}