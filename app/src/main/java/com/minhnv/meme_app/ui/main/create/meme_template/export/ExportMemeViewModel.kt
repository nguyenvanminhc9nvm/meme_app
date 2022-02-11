package com.minhnv.meme_app.ui.main.create.meme_template.export

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
import javax.inject.Inject


@HiltViewModel
class ExportMemeViewModel @Inject constructor(
    private val dataManager: AppDataManager
) : ViewModel() {
    private val _memeIcons = MutableLiveData<MutableList<MemeIcon>>()
    val memeIcons get() = _memeIcons

    private val _finalMeme = MutableLiveData<Bitmap>()
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

    fun getBitmapFromView(view: View) {
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
        _finalMeme.value = returnedBitmap
    }
}