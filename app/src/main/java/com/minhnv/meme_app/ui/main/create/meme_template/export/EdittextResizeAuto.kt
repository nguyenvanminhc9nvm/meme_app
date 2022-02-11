package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText

class EdittextResizeAuto @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    private var isNeedResize = false

    private var mTextSize = 50f

    private var mMaxTextSize = 0f

    private val mSpacingAdd = 0.0f

    private val mSpacingMult = 1.0f

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, after: Int) {
        val widthLimit = right - left - compoundPaddingLeft - compoundPaddingRight
        val heightLimit = bottom - top - compoundPaddingBottom - compoundPaddingTop
        resizeText(widthLimit, heightLimit)
    }

    private fun resizeText(widthView: Int, heightView: Int) {
        var value = text.toString()
        if (value.isEmpty() || widthView <= 0 || mTextSize == 0f) {
            return
        }
        if (transformationMethod != null) {
            value = transformationMethod.getTransformation(value, this).toString()
        }
        val textPaint = paint
        var targetTextSize = if (mMaxTextSize > 0) {
            mTextSize.coerceAtMost(mMaxTextSize)
        } else {
            mTextSize
        }
        var heightText = textHeight(value, textPaint, widthView, targetTextSize, heightView)
        while (heightText > heightView) {
            targetTextSize *= 0.9f
            heightText = textHeight(value, textPaint, widthView, targetTextSize, heightView)
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize)
        setLineSpacing(mSpacingAdd, mSpacingMult)
        isNeedResize = false
    }

    private fun textHeight(
        source: CharSequence,
        paint: TextPaint,
        widthView: Int,
        textSize: Float,
        height: Int
    ): Int {
        val paintCopy = TextPaint(paint)
        paintCopy.textSize = textSize
        val rect = Rect()
        paintCopy.getTextBounds(source.toString(), 0, source.count(), rect)
        val reduced = rect.height() / 5
        var heightConflict = 0
        if (reduced > 10) {
            heightConflict = height / reduced
        }
        val layout = StaticLayout(
            source,
            paintCopy,
            widthView,
            Layout.Alignment.ALIGN_NORMAL,
            mSpacingMult,
            mSpacingAdd,
            true
        )
        return layout.height - heightConflict
    }
}