package com.yjk.drawdiaryv2.common

import android.util.Log
import com.yjk.drawdiaryv2.BuildConfig

class YLog {

    private fun getTAG(): String? {
        var TAG = ""
        try {
            val stack = Throwable().fillInStackTrace()
            val trace = stack.stackTrace
            TAG = if (trace != null) {
                val className = trace[0].className
                val methodName = trace[0].methodName
                "[$className] $methodName"
            } else {
                "###YLOG"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return TAG
    }

    fun d(msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(getTAG(), msg!!)
        }
    }

    fun e(error: Exception?) {
        if (BuildConfig.DEBUG) {
            Log.e(getTAG(), "Exception", error)
        }
    }

    fun e(error: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(getTAG(), error!!)
        }
    }

    fun e(exception: Exception?, error: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(getTAG(), error, exception)
        }
    }


}