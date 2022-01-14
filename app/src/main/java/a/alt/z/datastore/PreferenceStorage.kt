package a.alt.z.datastore

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    val uiMode: Flow<UIMode>

    suspend fun updateUIMode(uiMode: UIMode)

    var completeOnboarding: Boolean
}