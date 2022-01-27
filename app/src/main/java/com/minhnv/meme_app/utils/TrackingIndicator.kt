package com.minhnv.meme_app.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingIndicator @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
)