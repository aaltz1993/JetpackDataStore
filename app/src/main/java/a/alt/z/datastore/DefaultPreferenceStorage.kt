package a.alt.z.datastore

import android.content.SharedPreferences
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DefaultPreferenceStorage(
    private val dataStore: DataStore<Preferences>,
    sharedPreferences: SharedPreferences
) : PreferenceStorage {

    override val uiMode = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PREF_KEY_UI_MODE]
                ?.let { UIMode.valueOf(it) }
                ?: UIMode.LIGHT
        }

    override suspend fun updateUIMode(uiMode: UIMode) {
        dataStore.edit { preferences -> preferences[PREF_KEY_UI_MODE] = uiMode.name }
    }

    override var completeOnboarding: Boolean by BooleanPreference(sharedPreferences, PREF_KEY_COMPLETE_ONBOARDING, false)

    companion object {
        private val PREF_KEY_UI_MODE = preferencesKey<String>("ui_mode")

        private const val PREF_KEY_COMPLETE_ONBOARDING = "complete_onboarding"
    }
}