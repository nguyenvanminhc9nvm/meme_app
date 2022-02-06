package com.minhnv.meme_app.ui.main.create

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(): ViewModel() {
    fun getFileSize(context: Context, fileUri: Uri): Long {
        val returnCursor = context.contentResolver.query(fileUri, null, null, null, null)
        val sizeIndex: Int = returnCursor?.getColumnIndex(OpenableColumns.SIZE) ?: 0
        returnCursor?.moveToFirst()
        return returnCursor?.getLong(sizeIndex) ?: 0L
    }
}