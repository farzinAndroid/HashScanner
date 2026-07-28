package com.example.hashscanner.ui.ui_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.hashscanner.viewmodel.DataStoreViewModel
import java.util.UUID

@Composable
fun CreateNewUUID(
    dataStoreViewModel: DataStoreViewModel,
    onUUIDCreated:(String)->Unit
) {

    LaunchedEffect(true) {
        if (dataStoreViewModel.getUUID() == null){
            val uuid = UUID.randomUUID().toString()
            dataStoreViewModel.saveUUID(uuid)
            onUUIDCreated(uuid)
        }else{
            val uuid = dataStoreViewModel.getUUID()
            onUUIDCreated(uuid!!)
        }
    }

}