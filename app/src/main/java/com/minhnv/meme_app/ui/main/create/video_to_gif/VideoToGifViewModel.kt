package com.minhnv.meme_app.ui.main.create.video_to_gif

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VideoToGifViewModel @Inject constructor() : ViewModel() {

    private val _startConvert = MutableLiveData<Boolean>()
    val startConvert get() = _startConvert

    private val _videoSelected = MutableLiveData<String>()
    val videoSelected get() = _videoSelected

    private val _outputVideo = MutableLiveData("")
    val outputVideo get() = _outputVideo

    fun getFileSize(context: Context, fileUri: Uri): Long {
        val returnCursor = context.contentResolver.query(fileUri, null, null, null, null)
        val sizeIndex: Int = returnCursor?.getColumnIndex(OpenableColumns.SIZE) ?: 0
        returnCursor?.moveToFirst()
        return returnCursor?.getLong(sizeIndex) ?: 0L
    }

    fun execute(inputVideo: String, outputVideo: String) {
        viewModelScope.launch {
            if (inputVideo.isEmpty()) {
                return@launch
            }
            _startConvert.value = true
            val file = File(outputVideo)
            if (file.exists()) {
                file.delete()
            }
            _videoSelected.value = inputVideo
            val query = convertVideoToGIF(inputVideo, outputVideo)
            val responseCode = withContext(Dispatchers.IO) { FFmpeg.execute(query) }
            if (responseCode == Config.RETURN_CODE_SUCCESS) {
                _outputVideo.value = outputVideo
            }
            _startConvert.value = false
        }
    }

    private fun convertVideoToGIF(inputVideo: String, output: String): Array<String> {
        val inputs: ArrayList<String> = ArrayList()
        inputs.apply {
            add("-i")
            add(inputVideo)
            add("-preset")
            add("ultrafast")
            add(output)
        }
        return inputs.toArray(arrayOfNulls<String>(inputs.size))
    }
}