package com.minhnv.meme_app.utils.text_recognizer

import android.content.Context

interface TextRecognitionBuilderImpl {
    fun setContext(context: Context): TextRecognitionBuilderImpl

    fun setCardType(type: CardType): TextRecognitionBuilderImpl

    fun setLangType(type: TextType): TextRecognitionBuilderImpl

    fun build(): TextRecognitionProcessor
}