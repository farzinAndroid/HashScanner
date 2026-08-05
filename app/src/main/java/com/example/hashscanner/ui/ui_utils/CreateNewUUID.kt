package com.example.hashscanner.ui.ui_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.hashscanner.viewmodel.AppViewModel
import java.util.UUID

@Composable
fun CreateNewUUID(
    appViewModel: AppViewModel,
    onUUIDCreated: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        val existingUuid = appViewModel.getDeviceId()
        if (existingUuid == null) {
            val newUuid = UUID.randomUUID().toString()
            appViewModel.saveDeviceId(newUuid)
            onUUIDCreated(newUuid)
        } else {
            onUUIDCreated(existingUuid)
        }
    }
}
