package a.alt.z.datastore

import android.content.SharedPreferences

class BooleanPreferenceLiveData constructor(
    sharedPreferences: SharedPreferences,
    key: String,
    defaultValue: Boolean
): PreferenceLiveData<Boolean>(sharedPreferences, key, defaultValue) {

    override fun getValueFromPreferences(
        sharedPreferences: SharedPreferences,
        key: String,
        defaultValue: Boolean
    ): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}