package com.minhnv.meme_app.utils.custom_view

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.minhnv.meme_app.R

class ProgressDialog constructor(context: Context) : Dialog(context) {
    init {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress)
        val window = window
        if (window != null) {
            getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}