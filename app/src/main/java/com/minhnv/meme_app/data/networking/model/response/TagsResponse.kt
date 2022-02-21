package com.minhnv.meme_app.data.networking.model.response

import com.google.gson.annotations.SerializedName

data class TagInfo(
    @SerializedName("name")
    val name: String?,
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("items")
    val items: MutableList<Community>
)
