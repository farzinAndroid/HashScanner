package com.example.hashscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.data.datastore.DataStoreRepoImpl
import com.example.hashscanner.data.network.ConnectivityObserver
import com.example.hashscanner.ui.navigation.Screens
import com.example.hashscanner.utils.Constants
import com.example.hashscanner.utils.Constants.IS_ACTIVATED_DATASTORE_ID
import com.example.hashscanner.utils.Constants.UUID_DATASTORE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AppViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val dataStoreRepo: DataStoreRepoImpl
) : ViewModel() {

    // --- Connectivity States ---
    val connectivityStatus: StateFlow<ConnectivityObserver.Status> =
        connectivityObserver.observe().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectivityObserver.Status.Unavailable
        )

    // --- Splash / Startup States ---
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _isChecking = MutableStateFlow(false)
    val isChecking = _isChecking.asStateFlow()

    private val _startDestination = MutableStateFlow<Screens>(Screens.Authentication)
    val startDestination = _startDestination.asStateFlow()

    init {
        performInitialCheck()
    }

    private fun performInitialCheck() {
        viewModelScope.launch {
            try {
                _isChecking.value = true

                // 1. UUID Initialization
                val existingUuid = dataStoreRepo.getString(UUID_DATASTORE_ID)
                if (existingUuid == null) {
                    val newUuid = java.util.UUID.randomUUID().toString()
                    dataStoreRepo.putString(newUuid, UUID_DATASTORE_ID)
                    Constants.UUID = newUuid
                } else {
                    Constants.UUID = existingUuid
                }

                // 2. Check Internet Connectivity with a 3-second timeout
                val status = withTimeoutOrNull(3000.milliseconds) {
                    connectivityObserver.observe().first()
                } ?: ConnectivityObserver.Status.Unavailable

                if (status != ConnectivityObserver.Status.Available) {
                    _startDestination.value = Screens.NoInternet
                } else {
                    // 3. Activation Check (Local only)
                    val isLocallyActivated = dataStoreRepo.getBoolean(IS_ACTIVATED_DATASTORE_ID) ?: false
                    
                    if (isLocallyActivated) {
                        _startDestination.value = Screens.Landing
                    } else {
                        _startDestination.value = Screens.Authentication
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _startDestination.value = Screens.NoInternet
            } finally {
                _isChecking.value = false
                delay(500)
                _isReady.value = true
            }
        }
    }

    fun retry() {
        viewModelScope.launch {
            if (_isChecking.value) return@launch

            _isChecking.value = true
            delay(1000)

            val status = connectivityObserver.observe().first()
            if (status == ConnectivityObserver.Status.Available) {
                _startDestination.value = Screens.Authentication
            }

            _isChecking.value = false
        }
    }

    // --- Identity / DataStore Logic ---
    fun saveUUID(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.putString(value, UUID_DATASTORE_ID)
        }
    }

    fun getUUID(): String? = runBlocking {
        dataStoreRepo.getString(UUID_DATASTORE_ID)
    }

    fun saveActivationStatus(isActivated: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.putBoolean(isActivated, IS_ACTIVATED_DATASTORE_ID)
        }
    }
}
