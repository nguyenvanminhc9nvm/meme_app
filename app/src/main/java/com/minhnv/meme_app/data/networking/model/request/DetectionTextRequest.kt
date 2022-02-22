package com.minhnv.meme_app.data.networking.model.request

import com.google.gson.annotations.SerializedName

data class DetectionTextRequest(
    @SerializedName("requests")
    val request: MutableList<Features>
)

data class Features(
    @SerializedName("features")
    val feature: MutableList<Type>,
    @SerializedName("image")
    val imageUrl: ImageDetection
)

data class ImageDetection(
    @SerializedName("source")
    val source: ImageDetectionUrl
)

data class ImageDetectionUrl(
    @SerializedName("imageUri")
    val imageUrl: String
)
data class Type(
    @SerializedName("type")
    val type: String = "TEXT_DETECTION"
)
