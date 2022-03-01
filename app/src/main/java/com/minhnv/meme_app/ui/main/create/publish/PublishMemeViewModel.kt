package com.minhnv.meme_app.ui.main.create.publish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhnv.meme_app.data.AppDataManager
import com.minhnv.meme_app.data.networking.model.response.UploadResponse
import com.minhnv.meme_app.utils.helper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PublishMemeViewModel @Inject constructor(
    private val dataManager: AppDataManager
) : ViewModel() {
    private val _uploadSuccess = MutableStateFlow<Resource<UploadResponse>>(Resource.empty())
    val uploadSuccess: StateFlow<Resource<UploadResponse>> get() = _uploadSuccess

    fun doUploadFile(
        title: String?,
        desc: String?,
        path: String
    ) {
        viewModelScope.launch {
            dataManager.doUploadFile(
                title,
                desc,
                MultipartBody.Part.createFormData(
                    "image",
                    File(path).name,
                    File(path).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            ).collect {
                _uploadSuccess.value = it
            }
        }
    }
}