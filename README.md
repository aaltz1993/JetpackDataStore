### Jetpack DataStore
: Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
<br/>

### DataStore
- Preferences DataStore
- Proto DataStore

### Preferences DataStore
```kotlin
class PreferencesStorage(context: Context) {

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
```

### Proto DataStore
```protobuf
syntax = "proto3";

option java_package = "a.alt.z.datastore";
option java_multiple_files = true;

message User {
  int64 id = 1;
  string name = 2;
}
```

```kotlin
object UserSerializer: Serializer<User> {

    override fun readFrom(input: InputStream): User {
        return try { User.parseFrom(input) }
        catch (exception: InvalidProtocolBufferException) { throw CorruptionException("failed deserializing protocol", exception) }
    }

    override fun writeTo(t: User, output: OutputStream) = t.writeTo(output)
}

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
```

<br/>

### SharedPreferences

```kotlin
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

class DefaultPreferenceStorage(sharedPreferences: SharedPreferences) : PreferenceStorage {
  
		override var completeOnboarding: Boolean by BooleanPreference(sharedPreferences, PREF_KEY_COMPLETE_ONBOARDING, false)

  	...
}

/* SharedPreferences + LiveData */
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
```

