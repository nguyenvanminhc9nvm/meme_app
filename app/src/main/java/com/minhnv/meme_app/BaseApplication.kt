package com.minhnv.meme_app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.android.material.color.DynamicColors
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private lateinit var mContext: Context

    companion object {
        const val KEY_THEMES = "KEY_THEMES"
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        sharedPreferences = applicationContext.getSharedPreferences("BaseApplication", Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()
        mContext = applicationContext
        DynamicColors.applyToActivitiesIfAvailable(this, R.style.Theme_Android2021_Dark)
    }

    fun preferencePutInteger(key: String?, value: Int) {
        sharedPreferencesEditor.putInt(key, value)
        sharedPreferencesEditor.commit()
    }

    fun preferenceGetInteger(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }


    fun clearPreference() {
        sharedPreferencesEditor.clear()
        sharedPreferencesEditor.commit()
    }
}