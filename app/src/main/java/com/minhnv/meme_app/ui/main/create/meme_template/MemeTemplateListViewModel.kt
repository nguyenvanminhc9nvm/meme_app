package com.minhnv.meme_app.ui.main.create.meme_template

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemeTemplateListViewModel @Inject constructor(
    private val dataManager: AppDataManager
) : ViewModel() {

    private val _memeTemplates = MutableLiveData<MutableList<MemeTemplate>>()
    val memeTemplates get() = _memeTemplates

    init {
        viewModelScope.launch {
            dataManager.doGetListMemeTemplates().let {
                _memeTemplates.value = it
            }
        }
    }
}