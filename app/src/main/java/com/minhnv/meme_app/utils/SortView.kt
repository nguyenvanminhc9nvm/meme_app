package com.minhnv.meme_app.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SortView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val bottomSort = BottomNavigationView(context)
    private val rycSort = RecyclerView(context)

    init {
        orientation = VERTICAL
        val bottomParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 2F)
        val params = LayoutParams(0, LayoutParams.MATCH_PARENT, 8F)
        addView(bottomSort, bottomParams)
        addView(rycSort, params)
    }
}