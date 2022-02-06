package com.minhnv.meme_app.data.networking.model.local

import android.graphics.Rect
import java.io.Serializable

data class MemeTemplate(
    val id: Int,
    val memeUrl: String,
    val areaRectText1st: Rect?,
    val areaRectText2nd: Rect?,
    val areaRectText3rd: Rect?,
    val areaRectText4th: Rect?
): Serializable {
    fun getListRect() = mutableListOf(
        areaRectText1st,
        areaRectText2nd,
        areaRectText3rd,
        areaRectText4th
    )
}