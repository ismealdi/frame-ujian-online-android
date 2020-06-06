package com.ismealdi.components

import android.util.Log

object Logs {
    fun e(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("AmError", "AmMsg: $msg")
        }
    }

    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("AmDebug", "AmMsg: $msg")
        }
    }

    fun v(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.v("AmVerbose", "AmMsg: $msg")
        }
    }

    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i("AmInfo", "AmMsg: $msg")
        }
    }

    fun w(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w("AmWarn", "AmMsg: $msg")
        }
    }

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("AmLog", "AmMsg: $msg")
        }
    }

    fun http(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("AmHttp", "AmMsg: $msg")
        }
    }
}