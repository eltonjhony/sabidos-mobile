package com.sabidos.infrastructure.logging

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sabidos.BuildConfig

class Logger private constructor(private val TAG: String) {

    private var priority: Int = Log.ERROR

    fun d(message: String): Logger {
        this.priority = Log.DEBUG
        if (BuildConfig.DEBUG) {
            Log.println(priority, TAG, message)
        }
        return this
    }

    fun e(message: String): Logger {
        this.priority = Log.ERROR
        if (BuildConfig.DEBUG) {
            Log.println(priority, TAG, message)
        }
        return this
    }

    fun i(message: String): Logger {
        this.priority = Log.INFO
        if (BuildConfig.DEBUG) {
            Log.println(priority, TAG, message)
        }
        return this
    }

    fun withCause(cause: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.println(priority, TAG, Log.getStackTraceString(cause))
        }

        if (priority == Log.ERROR) {
            FirebaseCrashlytics.getInstance().recordException(cause)
        }
    }

    fun withCause(error: Error?) {
        if (BuildConfig.DEBUG) {
            Log.println(priority, TAG, Log.getStackTraceString(error))
        }

        if (priority == Log.ERROR && error != null) {
            FirebaseCrashlytics.getInstance().recordException(error)
        }
    }

    companion object {
        fun withTag(tag: String): Logger {
            return Logger(tag)
        }
    }

}