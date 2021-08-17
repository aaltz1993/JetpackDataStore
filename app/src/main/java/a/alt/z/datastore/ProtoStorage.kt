package a.alt.z.datastore

import android.content.Context
import androidx.datastore.createDataStore
import androidx.datastore.preferences.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class ProtoStorage(context: Context) {

    private val dataStore = context.applicationContext.createDataStore(fileName = FILE_NAME, serializer = UserSerializer)

    val user = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                exception.printStackTrace()
                emit(User.getDefaultInstance())
            } else
                throw exception
        }

    suspend fun updateUserID(id: Long) {
        dataStore.updateData { preferences -> preferences.toBuilder().setId(id).build() }
    }

    suspend fun updateUserName(name: String) {
        dataStore.updateData { preferences -> preferences.toBuilder().setName(name).build() }
    }

    companion object {
        private const val FILE_NAME = "user.pb"
    }
}