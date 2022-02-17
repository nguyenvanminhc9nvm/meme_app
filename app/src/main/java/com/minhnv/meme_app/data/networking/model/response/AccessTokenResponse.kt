package com.minhnv.meme_app.data.networking.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("expires_in")
    val expiresIn: Double?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("scope")
    val scope: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("account_id")
    val accountId: Double?,
    @SerializedName("account_username")
    val accountUsername: String?
)
