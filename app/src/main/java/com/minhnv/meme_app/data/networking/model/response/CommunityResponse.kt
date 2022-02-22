package com.minhnv.meme_app.data.networking.model.response

import com.google.gson.annotations.SerializedName

data class Community(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("views")
    val views: Double?,
    @SerializedName("ups")
    val ups: Double?,
    @SerializedName("downs")
    val downs: Double?,
    @SerializedName("tags")
    val tags: MutableList<Tags>?,
    @SerializedName("images")
    val images: MutableList<Images>?
)

data class Tags(
    @SerializedName("name")
    val name: String?
)

data class Images(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("views")
    val views: Double?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("type")
    val type: String?
)

object TypeImages {
    const val IMAGE_JPEG = "image/jpeg"
    const val IMAGES_PNG = "image/png"
    const val IMAGES_GIF = "image/gif"
    const val VIDEO_MP4 = "video/mp4"
}