package a.alt.z.datastore

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BooleanPreference constructor(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean
): ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }
}