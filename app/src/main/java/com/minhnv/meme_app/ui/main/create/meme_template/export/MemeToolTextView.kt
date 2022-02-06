package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.minhnv.meme_app.R

class MemeToolTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var mode = 1
    private val linearToolHeader = LinearLayout(context).apply {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }
    private val linearToolArticle = LinearLayout(context).apply {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    private val btnAlignment = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_align_left)
        setOnClickListener {
            changeMode(1)
        }
        setPadding(10)
    }
    private val btnFont = TextView(context).apply {
        text = context.getText(R.string.aa)
        val typefaceBold = ResourcesCompat.getFont(context, R.font.poppins_bold)
        typeface = typefaceBold
        setOnClickListener {
            changeMode(2)
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
        setPadding(10)
    }
    private val btnColor = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_button_background_circle)
        backgroundTintList = ColorStateList.valueOf(Color.RED)
        setOnClickListener {
            changeMode(3)
        }
        setPadding(10)
    }
    private val btnTextBound = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_text_bound_filled)
        setOnClickListener {
            changeMode(4)
        }
        setPadding(10)
    }


    val btnAlignLeft = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_align_left)

    }
    val btnAlignCenter = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_align_center)

    }
    val btnAlignRight = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_align_right)

    }


    val btnFontRegular = TextView(context).apply {
        text = context.getText(R.string.aa)
        val typefaceBold = ResourcesCompat.getFont(context, R.font.poppins_regular)
        typeface = typefaceBold
        setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
    }
    val btnFontGothic = TextView(context).apply {
        text = context.getText(R.string.aa)
        val typefaceBold = ResourcesCompat.getFont(context, R.font.poppins_gothic)
        typeface = typefaceBold
        setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
    }
    val btnFontBold = TextView(context).apply {
        text = context.getText(R.string.aa)
        val typefaceBold = ResourcesCompat.getFont(context, R.font.poppins_bold)
        typeface = typefaceBold
        setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
    }


    val btnFontRed = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_button_background_circle_border)
        backgroundTintList = ColorStateList.valueOf(Color.RED)

    }
    val btnFontYellow = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_button_background_circle_border)
        backgroundTintList = ColorStateList.valueOf(Color.YELLOW)

    }
    val btnFontGreen = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_button_background_circle_border)
        backgroundTintList = ColorStateList.valueOf(Color.GREEN)

    }
    val btnFontWhite = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_button_background_circle_border)
    }


    val btnTextBoundStroke = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_text_bound_stroke)

    }
    val btnTextBoundFilled = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_text_bound_filled)
    }
    val btnTextBoundTextOnly = ImageView(context).apply {
        setBackgroundResource(R.drawable.ic_text_bound_text_only)
    }

    init {
        isEnabled = false
        this.orientation = VERTICAL
        val params = LayoutParams(80, 80)
        params.setMargins(50, 0, 100, 0)
        addView(linearToolHeader, LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f))
        addView(linearToolArticle, LayoutParams(LayoutParams.MATCH_PARENT, 0, 2f))

        linearToolHeader.addView(btnAlignment, params)
        linearToolHeader.addView(btnFont, params)
        linearToolHeader.addView(btnColor, params)
        linearToolHeader.addView(btnTextBound, params)

        linearToolArticle.addView(btnAlignLeft, params)
        linearToolArticle.addView(btnAlignCenter, params)
        linearToolArticle.addView(btnAlignRight, params)

        linearToolArticle.addView(btnFontRegular, params)
        linearToolArticle.addView(btnFontGothic, params)
        linearToolArticle.addView(btnFontBold, params)

        linearToolArticle.addView(btnFontRed, params)
        linearToolArticle.addView(btnFontYellow, params)
        linearToolArticle.addView(btnFontGreen, params)
        linearToolArticle.addView(btnFontWhite, params)

        linearToolArticle.addView(btnTextBoundStroke, params)
        linearToolArticle.addView(btnTextBoundFilled, params)
        linearToolArticle.addView(btnTextBoundTextOnly, params)

        when (mode) {
            1 -> {
                hideAlign(false)
                hideBound(true)
                hideColor(true)
                hideFont(true)
            }
            2 -> {
                hideAlign(true)
                hideBound(true)
                hideColor(true)
                hideFont(false)
            }
            3 -> {
                hideAlign(true)
                hideBound(true)
                hideColor(false)
                hideFont(true)
            }
            else -> {
                hideAlign(true)
                hideBound(false)
                hideColor(true)
                hideFont(true)
            }
        }
    }

    private fun changeMode(mode: Int) = when (mode) {
        1 -> {
            hideAlign(false)
            hideBound(true)
            hideColor(true)
            hideFont(true)
        }
        2 -> {
            hideAlign(true)
            hideBound(true)
            hideColor(true)
            hideFont(false)
        }
        3 -> {
            hideAlign(true)
            hideBound(true)
            hideColor(false)
            hideFont(true)
        }
        else -> {
            hideAlign(true)
            hideBound(false)
            hideColor(true)
            hideFont(true)
        }
    }

    private fun hideAlign(isHide: Boolean) {
        if (isHide) {
            btnAlignLeft.visibility = View.GONE
            btnAlignCenter.visibility = View.GONE
            btnAlignRight.visibility = View.GONE
        } else {
            btnAlignLeft.visibility = View.VISIBLE
            btnAlignCenter.visibility = View.VISIBLE
            btnAlignRight.visibility = View.VISIBLE
        }
    }

    private fun hideFont(isHide: Boolean) {
        if (isHide) {
            btnFontRegular.visibility = View.GONE
            btnFontGothic.visibility = View.GONE
            btnFontBold.visibility = View.GONE
        } else {
            btnFontRegular.visibility = View.VISIBLE
            btnFontGothic.visibility = View.VISIBLE
            btnFontBold.visibility = View.VISIBLE
        }
    }

    private fun hideColor(isHide: Boolean) {
        if (isHide) {
            btnFontRed.visibility = View.GONE
            btnFontYellow.visibility = View.GONE
            btnFontGreen.visibility = View.GONE
            btnFontWhite.visibility = View.GONE
        } else {
            btnFontRed.visibility = View.VISIBLE
            btnFontYellow.visibility = View.VISIBLE
            btnFontGreen.visibility = View.VISIBLE
            btnFontWhite.visibility = View.VISIBLE
        }
    }

    private fun hideBound(isHide: Boolean) {
        if (isHide) {
            btnTextBoundStroke.visibility = View.GONE
            btnTextBoundFilled.visibility = View.GONE
            btnTextBoundTextOnly.visibility = View.GONE
        } else {
            btnTextBoundStroke.visibility = View.VISIBLE
            btnTextBoundFilled.visibility = View.VISIBLE
            btnTextBoundTextOnly.visibility = View.VISIBLE
        }
    }
}