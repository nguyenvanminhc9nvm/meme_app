package com.minhnv.meme_app.ui.main

import androidx.lifecycle.ViewModel
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.utils.TrackingError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataManager: AppDataManager,
    private val trackingErrorHelper: TrackingError
) : ViewModel() {
}
