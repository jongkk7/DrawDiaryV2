package com.yjk.drawdiaryv2.common

import android.content.Context
import android.content.SharedPreferences

class SharedData(val context: Context) {
    private final val DB = "db"

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(DB, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "") ?: return ""
    }

    fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun setInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun setFloat(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.commit()
    }

    fun getFloat(key: String): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

}