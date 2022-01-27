package com.minhnv.meme_app.data.networking.model.request

import com.google.gson.annotations.SerializedName

data class ListRequest(
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int
)