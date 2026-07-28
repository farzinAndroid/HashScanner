package com.example.hashscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.data.datastore.DataStoreRepoImpl
import com.example.hashscanner.utils.Constants.UUID_DATASTORE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepoImpl
) : ViewModel() {


    fun saveUUID(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.putString(value, UUID_DATASTORE_ID)
        }
    }

    fun getUUID() = runBlocking {
        dataStoreRepo.getString(UUID_DATASTORE_ID)
    }


}