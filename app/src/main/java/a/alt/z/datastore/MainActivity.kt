package a.alt.z.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceStorage: PreferenceStorage

    private val protoStorage by lazy { UserDataStorage(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!preferenceStorage.completeOnboarding) {
            /* SHOW ONBOARDING */
        }

        setupObserver()
    }

    private fun setupObserver() {
        preferenceStorage.uiMode.asLiveData().observe(this) { uiMode ->
            when(uiMode) {
                UIMode.LIGHT -> {}
                UIMode.DARK -> {}
                else -> {}
            }
        }

        protoStorage.user.asLiveData().observe(this) { user ->
            val id = user.id
            val name = user.name
        }
    }
}