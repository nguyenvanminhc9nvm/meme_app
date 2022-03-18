package com.minhnv.meme_app.utils.text_recognizer

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage


abstract class VisionProcessorBase<T>(context: Context) {

    abstract fun stop()

    abstract fun detect(image: InputImage): Task<T>

    protected abstract fun onSuccess(results: T)

    protected abstract fun onFailure(e: Exception)
}