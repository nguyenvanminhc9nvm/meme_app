package com.minhnv.meme_app.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhnv.meme_app.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataManager: AppDataManager
) : ViewModel() {
    private val _themeSystemIsDark = MutableLiveData(false)
    val themeSystemIsDark get() = _themeSystemIsDark

    init {
        viewModelScope.launch {
            _themeSystemIsDark.value = dataManager.themeSystem()
        }
    }
    fun setThemeSystem(isDark: Boolean) {
        println("##themeAvailable: $isDark")
        viewModelScope.launch {
            dataManager.changeThemeSystem(isDark)
            _themeSystemIsDark.value = isDark
        }
    }
}