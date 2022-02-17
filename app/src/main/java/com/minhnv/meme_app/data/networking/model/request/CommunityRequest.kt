package com.minhnv.meme_app.data.networking.model.request

import com.google.gson.annotations.SerializedName

data class CommunityRequest(
    @SerializedName("section")
    val section: String,
    @SerializedName("sort")
    val sort: String,
    @SerializedName("page")
    val page: Int,
    @SerializedName("window")
    val window: String
)
