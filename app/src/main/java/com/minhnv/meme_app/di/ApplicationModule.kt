package com.minhnv.meme_app.di

import android.content.Context
import androidx.room.Room
import com.minhnv.meme_app.BuildConfig
import com.minhnv.meme_app.data.data_store.AppDataStoreHelper
import com.minhnv.meme_app.data.data_store.DataStoreHelper
import com.minhnv.meme_app.data.database.AppDatabase
import com.minhnv.meme_app.data.database.AppDbHelper
import com.minhnv.meme_app.data.database.DbHelper
import com.minhnv.meme_app.data.networking.ApiHelper
import com.minhnv.meme_app.data.networking.ApiService
import com.minhnv.meme_app.data.networking.AppApiHelper
import com.minhnv.meme_app.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun providerApiHost() = "https://api.imgur.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providerAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.DB_APP).build()

    @Provides
    @Singleton
    fun providerUserDao(db: AppDatabase) = db.userDao

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: AppApiHelper): ApiHelper = apiHelper

    @Provides
    @Singleton
    fun providerAppDataStore(@ApplicationContext appContext: Context) = AppDataStoreHelper(context = appContext)

    @Provides
    @Singleton
    fun providerAppDbHelper(appDatabase: AppDatabase) = AppDbHelper(appDatabase)

    @Provides
    @Singleton
    fun providerDbHelper(appDbHelper: AppDbHelper): DbHelper = appDbHelper

    @Provides
    @Singleton
    fun providerDataStore(appDataStoreHelper: AppDataStoreHelper): DataStoreHelper = appDataStoreHelper
}