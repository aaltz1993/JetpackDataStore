package a.alt.z.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferenceStorage(context: Context) {

    private val dataStore = context.applicationContext.createDataStore(DATA_STORE_NAME)

    val uiMode = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else
                throw exception
        }
        .map { preferences ->
            preferences[PREF_KEY_UI_MODE]
                ?.let { UIMode.valueOf(it) }
                ?: UIMode.LIGHT
        }

    suspend fun setUIMode(mode: UIMode)
            = dataStore.edit { preferences -> preferences[PREF_KEY_UI_MODE] = mode.name }

    companion object {
        const val DATA_STORE_NAME = "dstore"
        private val PREF_KEY_UI_MODE = preferencesKey<String>("ui_mode")
    }
}