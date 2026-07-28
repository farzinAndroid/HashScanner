package com.example.hashscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.data.network.ConnectivityObserver
import com.example.hashscanner.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    // Real-time connectivity status
    val connectivityStatus: StateFlow<ConnectivityObserver.Status> =
        connectivityObserver.observe().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectivityObserver.Status.Unavailable
        )

    // Splash screen  Startup logic states
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

                val status = withTimeoutOrNull(3000.milliseconds) {
                    connectivityObserver.observe().first()
                } ?: ConnectivityObserver.Status.Unavailable

                if (status != ConnectivityObserver.Status.Available) {
                    _startDestination.value = Screens.NoInternet
                } else {
                    // TODO: Add Auth and Server Health checks here later
                    _startDestination.value = Screens.Authentication
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
                // TODO: Add Auth and Server Health checks here later
                _startDestination.value = Screens.Authentication
            }

            _isChecking.value = false
        }
    }
}
