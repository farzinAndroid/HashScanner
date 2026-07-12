package com.example.hashscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.data.model.db_entities.PermissionInfo
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.model.db_entities.SuspiciousApp
import com.example.hashscanner.repository.AppDatabaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDatabaseViewmodel @Inject constructor(
    private val appDataBaseRepo: AppDatabaseRepo
) : ViewModel() {

    // --- State Flows for Observation ---
    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()

    private val _suspiciousApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val suspiciousApps: StateFlow<List<AppInfo>> = _suspiciousApps.asStateFlow()

    private val _scanHistory = MutableStateFlow<List<ScanHistory>>(emptyList())
    val scanHistory: StateFlow<List<ScanHistory>> = _scanHistory.asStateFlow()

    private val _lastScan = MutableStateFlow<ScanHistory?>(null)
    val lastScan: StateFlow<ScanHistory?> = _lastScan.asStateFlow()

    private val _allPermissions = MutableStateFlow<List<PermissionInfo>>(emptyList())
    val allPermissions: StateFlow<List<PermissionInfo>> = _allPermissions.asStateFlow()

    private val _allSuspiciousApps = MutableStateFlow<List<SuspiciousApp>>(emptyList())
    val allSuspiciousApps: StateFlow<List<SuspiciousApp>> = _allSuspiciousApps.asStateFlow()

    // --- AppDao Functions ---

    fun insertApp(app: AppInfo) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertApp(app)
    }

    fun insertAllApps(apps: List<AppInfo>) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertAllApps(apps)
    }

    fun updateApp(app: AppInfo) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.updateApp(app)
    }

    fun deleteApp(app: AppInfo) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteApp(app)
    }

    fun deleteAllApps() = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteAllApps()
    }

    fun getAllApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getAllApps()
    }

    fun getAppByPackage(pkg: String, onResult: (AppInfo?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.getAppByPackage(pkg))
    }

    fun getAppBySha256(hash: String, onResult: (AppInfo?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.getAppBySha256(hash))
    }

    fun getAppByMd5(md5: String, onResult: (AppInfo?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.getAppByMd5(md5))
    }

    fun getAppBySha1(sha1: String, onResult: (AppInfo?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.getAppBySha1(sha1))
    }

    fun countApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countApps())
    }

    fun getSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        _suspiciousApps.value = appDataBaseRepo.getSuspiciousApps()
    }

    fun getSafeApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getSafeApps()
    }

    fun getLowRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getLowRiskApps()
    }

    fun getMediumRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getMediumRiskApps()
    }

    fun getHighRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getHighRiskApps()
    }

    fun getCriticalApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getCriticalApps()
    }

    fun getAppsByRisk() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getAppsByRisk()
    }

    fun getNewestApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getNewestApps()
    }

    fun getRecentlyUpdatedApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getRecentlyUpdatedApps()
    }

    fun getAppsByInstaller(installer: String) = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getAppsByInstaller(installer)
    }

    fun getDebuggableApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getDebuggableApps()
    }

    fun getDisabledApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getDisabledApps()
    }

    fun getOldTargetSdkApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getOldTargetSdkApps()
    }

    fun getAppsByCertificateSha256(sha256: String) = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getAppsByCertificateSha256(sha256)
    }

    fun searchApps(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.searchApps(keyword)
    }

    fun getLargestApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getLargestApps()
    }

    fun getSmallestApps() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getSmallestApps()
    }

    fun getRecommendedForUpload() = viewModelScope.launch(Dispatchers.IO) {
        _allApps.value = appDataBaseRepo.getRecommendedForUpload()
    }

    fun countRecommendedUploads(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countRecommendedUploads())
    }

    fun getRecommendedSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        _suspiciousApps.value = appDataBaseRepo.getRecommendedSuspiciousApps()
    }

    fun countSystemApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countSystemApps())
    }

    fun countUserApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countUserApps())
    }

    fun countSuspiciousApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countSuspiciousApps())
    }

    fun countSafeApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countSafeApps())
    }

    fun countLowRiskApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countLowRiskApps())
    }

    fun countMediumRiskApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countMediumRiskApps())
    }

    fun countHighRiskApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countHighRiskApps())
    }

    fun countCriticalApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countCriticalApps())
    }

    // --- PermissionDao Functions ---

    fun insertPermission(permission: PermissionInfo) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertPermission(permission)
    }

    fun insertAllPermissions(list: List<PermissionInfo>) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertAllPermissions(list)
    }

    fun getPermissions(pkg: String) = viewModelScope.launch(Dispatchers.IO) {
        _allPermissions.value = appDataBaseRepo.getPermissions(pkg)
    }

    fun deleteAllPermissions() = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteAllPermissions()
    }

    // --- SuspiciousDao Functions ---

    fun insertSuspiciousApp(app: SuspiciousApp) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertSuspiciousApp(app)
    }

    fun insertAllSuspiciousApps(apps: List<SuspiciousApp>) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertAllSuspiciousApps(apps)
    }

    fun updateSuspiciousApp(app: SuspiciousApp) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.updateSuspiciousApp(app)
    }

    fun deleteSuspiciousApp(app: SuspiciousApp) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteSuspiciousApp(app)
    }

    fun deleteAllSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteAllSuspiciousApps()
    }

    fun getAllSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        _allSuspiciousApps.value = appDataBaseRepo.getAllSuspiciousApps()
    }

    fun getSuspiciousAppByPackage(pkg: String, onResult: (SuspiciousApp?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.getSuspiciousAppByPackage(pkg))
    }

    fun getNotSentSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        _allSuspiciousApps.value = appDataBaseRepo.getNotSentSuspiciousApps()
    }

    fun getRecommendedSuspicious() = viewModelScope.launch(Dispatchers.IO) {
        _allSuspiciousApps.value = appDataBaseRepo.getRecommendedSuspicious()
    }

    fun countSuspiciousTotal(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countSuspiciousTotal())
    }

    fun countRecommendedSuspicious(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countRecommendedSuspicious())
    }

    fun countNotSentSuspicious(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onResult(appDataBaseRepo.countNotSentSuspicious())
    }

    fun markSuspiciousUploaded(pkg: String, date: String) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.markSuspiciousUploaded(pkg, date)
    }

    // --- ScanHistoryDao Functions ---

    fun insertScanHistory(history: ScanHistory) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertScanHistory(history)
    }

    fun getAllScanHistory() = viewModelScope.launch(Dispatchers.IO) {
        _scanHistory.value = appDataBaseRepo.getAllScanHistory()
    }

    fun getLastScan() = viewModelScope.launch(Dispatchers.IO) {
        _lastScan.value = appDataBaseRepo.getLastScan()
    }

    fun deleteAllScanHistory() = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteAllScanHistory()
    }
}