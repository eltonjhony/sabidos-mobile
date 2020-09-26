package com.sabidos.infrastructure.helpers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * An abstract wrapper class for [SharedPreferences].
 */

abstract class PrefsHelper(private val application: Application) {

    constructor(ctx: Context) : this(application = ctx.applicationContext as Application)

    /**
     * This is called to obtain the map of entries in the associated [SharedPreferences].
     */
    val allEntries: Map<String, *>
        get() = providePreferences().all

    /**
     * A wrapper around [SharedPreferences.getBoolean]
     */
    fun getBoolean(id: String, default: Boolean) = providePreferences().getBoolean(id, default)

    /**
     * A wrapper around [SharedPreferences.getLong]
     */
    fun getLong(id: String, defaultVal: Long): Long = providePreferences().getLong(id, defaultVal)

    /**
     * A wrapper around [SharedPreferences.getString]
     */
    fun getString(id: String, defaultVal: String) =
        providePreferences().getString(id, defaultVal).orEmpty()

    fun getString(id: String) = providePreferences().getString(id, null)

    /**
     * A wrapper around [SharedPreferences.getInt]
     */
    fun getInt(id: String, defaultVal: Int) = providePreferences().getInt(id, defaultVal)

    /**
     * A wrapper around [android.content.SharedPreferences.Editor.putBoolean]
     */
    fun putBoolean(id: String, value: Boolean, commit: Boolean = false) {
        providePreferences().edit(commit) { putBoolean(id, value) }
    }

    /**
     * A wrapper around [android.content.SharedPreferences.Editor.putString]
     */
    fun putString(id: String, value: String, commit: Boolean = false) {
        providePreferences().edit(commit) { putString(id, value) }
    }

    /**
     * A wrapper around [android.content.SharedPreferences.Editor.putInt]
     */
    fun putInt(id: String, value: Int, commit: Boolean = false) {
        providePreferences().edit(commit) { putInt(id, value) }
    }

    /**
     * A wrapper around [android.content.SharedPreferences.Editor.putLong]
     */
    fun putLong(id: String, value: Long, commit: Boolean = false) {
        providePreferences().edit(commit) { putLong(id, value) }
    }

    fun putBoolean(id: String, value: Boolean) = putBoolean(id, value, true)
    fun putString(id: String, value: String) = putString(id, value, true)
    fun putInt(id: String, value: Int) = putInt(id, value, true)
    fun putLong(id: String, value: Long) = putLong(id, value, true)
    fun clearAllSharedPreferences() = clearAllSharedPreferences(true)
    /**
     * Clears all the data from the [SharedPreferences]
     */
    open fun clearAllSharedPreferences(commit: Boolean = false) {
        providePreferences().edit(commit) { clear() }
    }

    /**
     *  A wrapper around [android.content.SharedPreferences.Editor.remove]
     */
    fun remove(key: String, commit: Boolean = false) {
        providePreferences().edit(commit) { remove(key) }
    }

    /**
     *  Allows us to watch for changes in prefs, by key, etc.
     */
    fun registerOnChangedListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        providePreferences().registerOnSharedPreferenceChangeListener(listener)
    }

    /**
     *  Unregister Listener
     */
    fun unRegisterOnChangedListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        providePreferences().unregisterOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Returns the [SharedPreferences] that this class wraps.
     */
    @Synchronized
    protected open fun providePreferences(): SharedPreferences =
        application.getSharedPreferences(providePreferencesString(), Context.MODE_PRIVATE)

    /**
     * This [String] identifies the [SharedPreferences] that will be used.
     */
    protected abstract fun providePreferencesString(): String
}