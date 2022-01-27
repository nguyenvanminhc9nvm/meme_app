package com.minhnv.meme_app.ui.main.profile

import androidx.lifecycle.ViewModel
import com.minhnv.meme_app.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataManager: AppDataManager
) : ViewModel() {

}