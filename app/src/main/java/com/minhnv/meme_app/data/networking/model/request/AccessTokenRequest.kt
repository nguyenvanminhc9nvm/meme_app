package com.minhnv.meme_app.data.networking.model.request

import com.google.gson.annotations.SerializedName

data class AccessTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("client_id")
    val clientId: String?,
    @SerializedName("client_secret")
    val clientSecret: String?,
    @SerializedName("grant_type")
    val grantType: String?
)
