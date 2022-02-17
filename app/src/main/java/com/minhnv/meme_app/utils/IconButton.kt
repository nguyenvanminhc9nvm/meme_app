package com.minhnv.meme_app.utils

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.minhnv.meme_app.R

class IconButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val tvTitle = TextView(context).apply {
        val typeFaceBold = ResourcesCompat.getFont(context, R.font.poppins_bold)
        typeface = typeFaceBold
        textAlignment = TEXT_ALIGNMENT_CENTER
    }
    private val imgIcon = ImageView(context)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.IconButton, 0, 0).apply {
            try {
                tvTitle.text = getString(R.styleable.IconButton_setText)
                imgIcon.setImageResource(
                    getResourceId(
                        R.styleable.IconButton_setIcon, R.drawable.ic_button_up
                    )
                )
            } finally {
                recycle()
            }
        }
        setPadding(20)
        gravity = Gravity.CENTER_VERTICAL
        setBackgroundResource(R.drawable.ic_button_stroke)
        orientation = VERTICAL
        val params = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1F)
        addView(imgIcon, params)
        addView(tvTitle, params)
        invalidate()
    }

    fun setText(text: String) {
        tvTitle.text = text
    }
}