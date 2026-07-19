package com.example.hashscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.repository.NetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewmodel @Inject constructor(
    private val networkRepo: NetworkRepo
) : ViewModel() {

    fun uploadPending() =viewModelScope.launch(Dispatchers.IO) {
        networkRepo.uploadPending()
    }
}