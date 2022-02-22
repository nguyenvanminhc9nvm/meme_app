package com.minhnv.meme_app.data.networking.model.response

import com.google.gson.annotations.SerializedName

data class DetectionTextResponse(
    @SerializedName("responses")
    val response: MutableList<Responses>?
)

data class Responses(
    @SerializedName("textAnnotations")
    val textAnnotations: MutableList<TextAnnotations>?
)

data class TextAnnotations(
    @SerializedName("locale")
    val locale: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("boundingPoly")
    val boundingPoly: BoundingPoly
)

data class BoundingPoly(
    @SerializedName("vertices")
    val vertices: MutableList<Vertices>?
)

data class Vertices(
    @SerializedName("x")
    val x: Int?,
    @SerializedName("y")
    val y: Int?,
)