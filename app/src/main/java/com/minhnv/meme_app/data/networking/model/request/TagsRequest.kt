package com.minhnv.meme_app.data.networking.model.request

import com.google.gson.annotations.SerializedName

data class TagsRequest(
    @SerializedName("tagName")
    val tagName: String,
    @SerializedName("sort")
    val sort: String,
    @SerializedName("window")
    val window: String,
    @SerializedName("page")
    val page: Int
)
