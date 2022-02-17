package com.minhnv.meme_app.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.minhnv.meme_app.data.networking.model.response.ErrorResponse
import com.minhnv.meme_app.utils.validate.ValidateException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingError @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
) {
    fun coroutineExceptionHandler() = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is HttpException -> {
                val gson = Gson()
                val errorBody = exception.response()?.errorBody()?.string()
                val response = gson.fromJson(errorBody, ErrorResponse::class.java)
                response.data?.data?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
            is ValidateException -> {
                Toast.makeText(applicationContext, exception.message, Toast.LENGTH_SHORT).show()
            }

        }
    }
}