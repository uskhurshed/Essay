package com.easyapps.zkplayer.utils

import android.content.Context
import com.easyapps.zkplayer.BuildConfig

object PreferencesUtil {
    fun getFromPref(context: Context, key: String, defValue: String?): String? {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return prefs.getString(key, defValue)
    }

    fun setFromPref(context: Context, key: String, value: String?) {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    fun getPrefBool(context: Context, key: String, defValue: Boolean): Boolean {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, defValue)
    }

    fun setPrefBool(context: Context, key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
    }
    fun getPrefInt(context: Context, key: String, defValue: Int): Int {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return prefs.getInt(key, defValue)
    }

    fun setPrefInt(context: Context, key: String, value: Int) {
        val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }
}