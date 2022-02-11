package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.view.Gravity
import com.minhnv.meme_app.R

data class MemeToolIcon(
    val id: Int,
    val type: TypedValueTool,
    val icon: Int? = null,
    val attributes: Int? = null,
    val colorPrimary: Int? = null,
    val colorSecondary: Int? = null,
    val text: String? = null
)

data class MemeIcon(
    val id: Int,
    val meme: Int
)

enum class TypedValueTool {
    ALIGN, COLOR, SIZE, SPACING, FONT, BOUND
}

val memeToolsAlign = mutableListOf(
    MemeToolIcon(
        1,
        TypedValueTool.ALIGN,
        R.drawable.ic_align_left,
        Gravity.START or Gravity.CENTER_VERTICAL
    ),
    MemeToolIcon(
        id = 2,
        TypedValueTool.ALIGN,
        icon = R.drawable.ic_align_center,
        attributes = Gravity.CENTER or Gravity.CENTER_VERTICAL
    ),
    MemeToolIcon(
        id = 3,
        TypedValueTool.ALIGN,
        icon = R.drawable.ic_align_right,
        attributes = Gravity.END or Gravity.CENTER_VERTICAL
    ),
    MemeToolIcon(
        id = 4,
        TypedValueTool.ALIGN,
        icon = R.drawable.ic_align_bottom,
        attributes = Gravity.BOTTOM
    ),
    MemeToolIcon(
        id = 5,
        TypedValueTool.ALIGN,
        icon = R.drawable.ic_align_top,
        attributes = Gravity.TOP
    ),
    MemeToolIcon(
        id = 6,
        TypedValueTool.ALIGN,
        icon = R.drawable.ic_align_center_text,
        attributes = Gravity.CENTER
    )
)

val memeIconsFont = mutableListOf(
    MemeToolIcon(
        id = 1,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.black,
        colorSecondary = R.color.overlay_material_1_primary,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 2,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.orange,
        colorSecondary = R.color.green,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 3,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.blue,
        colorSecondary = R.color.violet,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 4,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.green,
        colorSecondary = R.color.black,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 5,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.red,
        colorSecondary = R.color.gray,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 6,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.blue,
        colorSecondary = R.color.red,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 7,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.pink,
        colorSecondary = R.color.violet_2,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 8,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.blue_8,
        colorSecondary = R.color.green_8,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 9,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.gray,
        colorSecondary = R.color.red,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 10,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.white,
        colorSecondary = R.color.black,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 11,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.orange_2,
        colorSecondary = R.color.blue_3,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 10,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.green_9,
        colorSecondary = R.color.blue_4,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 10,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.yellow,
        colorSecondary = R.color.red,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 10,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.green,
        colorSecondary = R.color.red,
        text = "Aa"
    ),
    MemeToolIcon(
        id = 10,
        type = TypedValueTool.COLOR,
        icon = null,
        attributes = null,
        colorPrimary = R.color.violet_3,
        colorSecondary = R.color.orange_2,
        text = "Aa"
    )
)

val memeIconsSpacing = mutableListOf(
    MemeToolIcon(
        1,
        TypedValueTool.SIZE,
        null,
        com.intuit.sdp.R.dimen._10sdp,
        null,
        null,
        "10"
    ),
    MemeToolIcon(
        2,
        TypedValueTool.SIZE,
        null,
        com.intuit.sdp.R.dimen._14sdp,
        null,
        null,
        "14"
    ),
    MemeToolIcon(
        3,
        TypedValueTool.SIZE,
        null,
        com.intuit.sdp.R.dimen._18sdp,
        null,
        null,
        "18"
    ),
    MemeToolIcon(
        4,
        TypedValueTool.SIZE,
        null,
        com.intuit.sdp.R.dimen._22sdp,
        null,
        null,
        "22"
    ),
    MemeToolIcon(
        5,
        TypedValueTool.SIZE,
        null,
        com.intuit.sdp.R.dimen._26sdp,
        null,
        null,
        "26"
    ),
    MemeToolIcon(
        6,
        TypedValueTool.SPACING,
        R.drawable.ic_spacing_text_1,
        com.intuit.sdp.R.dimen._1sdp,
        null,
        null,
        null
    ),
    MemeToolIcon(
        7,
        TypedValueTool.SPACING,
        R.drawable.ic_spacing_text_2,
        com.intuit.sdp.R.dimen._3sdp,
        null,
        null,
        null
    ),
    MemeToolIcon(
        8,
        TypedValueTool.SPACING,
        R.drawable.ic_spacing_text_3,
        com.intuit.sdp.R.dimen._5sdp,
        null,
        null,
        null
    ),
    MemeToolIcon(
        9,
        TypedValueTool.SPACING,
        R.drawable.ic_spacing_text_4,
        com.intuit.sdp.R.dimen._7sdp,
        null,
        null,
        null
    ),
    MemeToolIcon(
        10,
        TypedValueTool.SPACING,
        R.drawable.ic_spacing_text_5,
        com.intuit.sdp.R.dimen._9sdp,
        null,
        null,
        null
    ),
    MemeToolIcon(
        11,
        TypedValueTool.FONT,
        null,
        R.font.poppins_gothic,
        null,
        null,
        "Aa"
    ),
    MemeToolIcon(
        12,
        TypedValueTool.FONT,
        null,
        R.font.poppins_regular,
        null,
        null,
        "Aa"
    ),
    MemeToolIcon(
        13,
        TypedValueTool.FONT,
        null,
        R.font.poppins_medium,
        null,
        null,
        "Aa"
    ),
    MemeToolIcon(
        14,
        TypedValueTool.FONT,
        null,
        R.font.poppins_bold,
        null,
        null,
        "Aa"
    ),
    MemeToolIcon(
        15,
        TypedValueTool.FONT,
        null,
        R.font.poppins_extra_bold,
        null,
        null,
        "Aa"
    )
)

val memeIcons = mutableListOf(
    MemeIcon(
        1,
        R.drawable.ic_meme_icon_1
    ),
    MemeIcon(
        2,
        R.drawable.ic_meme_icon_2
    ),
    MemeIcon(
        3,
        R.drawable.ic_meme_icon_3
    ),
    MemeIcon(
        4,
        R.drawable.ic_meme_icon_4
    ),
    MemeIcon(
        5,
        R.drawable.ic_meme_icon_5
    ),
    MemeIcon(
        6,
        R.drawable.ic_meme_icon_6
    ),
    MemeIcon(
        7,
        R.drawable.ic_meme_icon_7
    ),
    MemeIcon(
        8,
        R.drawable.ic_meme_icon_8
    ),
    MemeIcon(
        9,
        R.drawable.ic_meme_icon_9
    ),
    MemeIcon(
        10,
        R.drawable.ic_meme_icon_10
    ),
    MemeIcon(
        11,
        R.drawable.ic_meme_icon_11
    ),
    MemeIcon(
        12,
        R.drawable.ic_meme_icon_12
    ),
    MemeIcon(
        13,
        R.drawable.ic_meme_icon_13
    ),
    MemeIcon(
        14,
        R.drawable.ic_meme_icon_14
    ),
    MemeIcon(
        15,
        R.drawable.ic_meme_icon_15
    ),
    MemeIcon(
        16,
        R.drawable.ic_meme_icon_16
    ),
    MemeIcon(
        17,
        R.drawable.ic_meme_icon_17
    ),
    MemeIcon(
        18,
        R.drawable.ic_meme_icon_18
    ),
    MemeIcon(
        19,
        R.drawable.ic_meme_icon_19
    ),
    MemeIcon(
        20,
        R.drawable.ic_meme_icon_20
    )
)

val memeIconsBound = mutableListOf(
    MemeToolIcon(
        id = 16,
        type = TypedValueTool.BOUND,
        icon = R.drawable.ic_text_bound_filled,
        attributes = R.drawable.ic_text_bound_background_filled,
        colorPrimary = R.color.red
    ),
    MemeToolIcon(
        id = 16,
        type = TypedValueTool.BOUND,
        icon = R.drawable.ic_text_bound_filled,
        attributes = R.drawable.ic_text_bound_background_filled,
        colorPrimary = R.color.green
    ), MemeToolIcon(
        id = 16,
        type = TypedValueTool.BOUND,
        icon = R.drawable.ic_text_bound_filled,
        attributes = R.drawable.ic_text_bound_background_filled,
        colorPrimary = R.color.black
    ), MemeToolIcon(
        id = 16,
        type = TypedValueTool.BOUND,
        icon = R.drawable.ic_text_bound_filled,
        attributes = R.drawable.ic_text_bound_background_filled,
        colorPrimary = R.color.white
    ), MemeToolIcon(
        id = 16,
        type = TypedValueTool.BOUND,
        icon = R.drawable.ic_text_bound_filled,
        attributes = R.drawable.ic_text_bound_background_filled,
        colorPrimary = R.color.yellow
    ),
    MemeToolIcon(
        id = 16,
        type = TypedValueTool.BOUND,
        icon = R.drawable.ic_text_bound_text_only,
        attributes = R.drawable.ic_text_bound_background_text_only,
        colorPrimary = android.R.color.transparent
    )
)
