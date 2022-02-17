package com.minhnv.meme_app.data.networking.model.response

import com.google.gson.annotations.SerializedName

open class Response<T> {
    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: T? = null

    @SerializedName("status")
    var status: Int? = null

    fun isSuccess(): Boolean {
        return success == true
    }
}

class ListResponse<T> {
    @SerializedName("list")
    var list: MutableList<T>? = null
}

data class ErrorData(
    @SerializedName("error")
    val data: String
)

class ErrorResponse : Response<ErrorData>()
class CommunityResponse: Response<MutableList<Community>>()