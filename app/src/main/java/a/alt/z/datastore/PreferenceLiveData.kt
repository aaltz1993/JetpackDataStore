package a.alt.z.datastore

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

abstract class PreferenceLiveData<T> constructor(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: T
): LiveData<T>() {

    private val onSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (this.key == key) {
            getValueFromPreferences(sharedPreferences, key, defaultValue)
        }
    }

    abstract fun getValueFromPreferences(sharedPreferences: SharedPreferences, key: String, defaultValue: T): T

    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(sharedPreferences, key, defaultValue)
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    override fun onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        super.onInactive()
    }
}