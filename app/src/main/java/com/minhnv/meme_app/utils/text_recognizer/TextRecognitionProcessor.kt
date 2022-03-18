package com.minhnv.meme_app.utils.text_recognizer

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

enum class CardType {
    CARD_TYPE_1, CARD_TYPE_2, CARD_TYPE_3, VN_CITIZEN_ID
}

enum class TextType {
    TEXT_RECOGNITION_LATIN, TEXT_RECOGNITION_JP
}


class TextRecognitionProcessor private constructor(
    context: Context,
    private val textType: TextType,
    private val cardType: CardType
) : VisionProcessorBase<Text>(context) {

    private var mContext: Context? = null
    private var textRecognizer: TextRecognizer

    init {
        mContext = context
        textRecognizer = TextRecognition.getClient(setTextRecognizerOptions())
    }

    class Builder : TextRecognitionBuilderImpl {

        private var builderContext: Context? = null
        private var textType: TextType = TextType.TEXT_RECOGNITION_LATIN
        private var cardType: CardType = CardType.CARD_TYPE_1

        override fun setContext(context: Context): TextRecognitionBuilderImpl {
            builderContext = context
            return this
        }

        override fun setCardType(type: CardType): TextRecognitionBuilderImpl {
            cardType = type
            return this
        }

        override fun setLangType(type: TextType): TextRecognitionBuilderImpl {
            textType = type
            return this
        }

        override fun build(): TextRecognitionProcessor {
            return TextRecognitionProcessor(builderContext!!, textType, cardType)
        }

    }

    private fun setTextRecognizerOptions(): TextRecognizerOptionsInterface {
        return when (textType) {
            TextType.TEXT_RECOGNITION_LATIN -> {
                TextRecognizerOptions.Builder().build()
            }
            TextType.TEXT_RECOGNITION_JP -> {
                JapaneseTextRecognizerOptions.Builder().build()
            }
        }
    }

    override fun stop() {
        textRecognizer.close()
        mContext = null
    }

    override fun onSuccess(results: Text) {

    }

    override fun onFailure(e: Exception) {
        // Show Exception
    }

    override fun detect(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }
}
