package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhnv.meme_app.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ExportMemeViewModel @Inject constructor(
    private val dataManager: AppDataManager
) : ViewModel() {
    private val _memeIcons = MutableLiveData<MutableList<MemeIcon>>()
    val memeIcons get() = _memeIcons

    private val _finalMeme = MutableLiveData<String>()
    val finalMeme get() = _finalMeme

    private val _memeEdtValue = MutableLiveData<MutableMap<Int, Pair<Int, Rect>>>()
    val memeEdtValue get() = _memeEdtValue

    private val memeEdtValueMaple = mutableMapOf<Int, Pair<Int, Rect>>()

    init {
        viewModelScope.launch {
            _memeIcons.value = dataManager.doGetListMemeIcon()
        }
    }

    fun setMemeEdtValue(memeIcon: MemeIcon, rect: Rect) {
        memeEdtValueMaple[memeIcon.id] = Pair(memeIcon.meme, rect)
        _memeEdtValue.value = memeEdtValueMaple
    }

    fun storeAndGetPathImage(view: View, context: Context): String {
        val returnedBitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        val bgDrawable: Drawable? = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return storeImage(returnedBitmap, context)
    }

    private fun storeImage(image: Bitmap, context: Context): String {
        val pictureFile = getOutputMediaFile(context) ?: return ""
        return try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            pictureFile.path
        } catch (e: IOException) {
            ""
        }
    }

    private fun getOutputMediaFile(context: Context): File? {
        val mediaStorageDir = File(context.filesDir.path)
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        } else {
            mediaStorageDir.delete()
        }
        val mediaFile: File
        val mImageName = "MI_meme_created.jpg"
        mediaFile = File(mediaStorageDir.path + File.separator.toString() + mImageName)
        return mediaFile
    }
}