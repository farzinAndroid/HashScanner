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

    // --- New Specific Flows for AppInfo ---
    private val _appByPackage = MutableStateFlow<AppInfo?>(null)
    val appByPackage: StateFlow<AppInfo?> = _appByPackage.asStateFlow()

    private val _appBySha256 = MutableStateFlow<AppInfo?>(null)
    val appBySha256: StateFlow<AppInfo?> = _appBySha256.asStateFlow()

    private val _appByMd5 = MutableStateFlow<AppInfo?>(null)
    val appByMd5: StateFlow<AppInfo?> = _appByMd5.asStateFlow()

    private val _appBySha1 = MutableStateFlow<AppInfo?>(null)
    val appBySha1: StateFlow<AppInfo?> = _appBySha1.asStateFlow()

    private val _safeApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val safeApps: StateFlow<List<AppInfo>> = _safeApps.asStateFlow()

    private val _lowRiskApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val lowRiskApps: StateFlow<List<AppInfo>> = _lowRiskApps.asStateFlow()

    private val _mediumRiskApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val mediumRiskApps: StateFlow<List<AppInfo>> = _mediumRiskApps.asStateFlow()

    private val _highRiskApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val highRiskApps: StateFlow<List<AppInfo>> = _highRiskApps.asStateFlow()

    private val _criticalApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val criticalApps: StateFlow<List<AppInfo>> = _criticalApps.asStateFlow()

    private val _appsByRisk = MutableStateFlow<List<AppInfo>>(emptyList())
    val appsByRisk: StateFlow<List<AppInfo>> = _appsByRisk.asStateFlow()

    private val _newestApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val newestApps: StateFlow<List<AppInfo>> = _newestApps.asStateFlow()

    private val _recentlyUpdatedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val recentlyUpdatedApps: StateFlow<List<AppInfo>> = _recentlyUpdatedApps.asStateFlow()

    private val _appsByInstaller = MutableStateFlow<List<AppInfo>>(emptyList())
    val appsByInstaller: StateFlow<List<AppInfo>> = _appsByInstaller.asStateFlow()

    private val _debuggableApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val debuggableApps: StateFlow<List<AppInfo>> = _debuggableApps.asStateFlow()

    private val _disabledApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val disabledApps: StateFlow<List<AppInfo>> = _disabledApps.asStateFlow()

    private val _oldTargetSdkApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val oldTargetSdkApps: StateFlow<List<AppInfo>> = _oldTargetSdkApps.asStateFlow()

    private val _appsByCertificateSha256 = MutableStateFlow<List<AppInfo>>(emptyList())
    val appsByCertificateSha256: StateFlow<List<AppInfo>> = _appsByCertificateSha256.asStateFlow()

    private val _searchResultApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val searchResultApps: StateFlow<List<AppInfo>> = _searchResultApps.asStateFlow()

    private val _largestApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val largestApps: StateFlow<List<AppInfo>> = _largestApps.asStateFlow()

    private val _smallestApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val smallestApps: StateFlow<List<AppInfo>> = _smallestApps.asStateFlow()

    private val _recommendedForUpload = MutableStateFlow<List<AppInfo>>(emptyList())
    val recommendedForUpload: StateFlow<List<AppInfo>> = _recommendedForUpload.asStateFlow()

    private val _recommendedSuspiciousApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val recommendedSuspiciousApps: StateFlow<List<AppInfo>> = _recommendedSuspiciousApps.asStateFlow()

    // --- New Specific Flows for Counts ---
    private val _appsCount = MutableStateFlow<Int>(0)
    val appsCount: StateFlow<Int> = _appsCount.asStateFlow()

    private val _recommendedUploadsCount = MutableStateFlow<Int>(0)
    val recommendedUploadsCount: StateFlow<Int> = _recommendedUploadsCount.asStateFlow()

    private val _systemAppsCount = MutableStateFlow<Int>(0)
    val systemAppsCount: StateFlow<Int> = _systemAppsCount.asStateFlow()

    private val _userAppsCount = MutableStateFlow<Int>(0)
    val userAppsCount: StateFlow<Int> = _userAppsCount.asStateFlow()

    private val _suspiciousAppsCount = MutableStateFlow<Int>(0)
    val suspiciousAppsCount: StateFlow<Int> = _suspiciousAppsCount.asStateFlow()

    private val _safeAppsCount = MutableStateFlow<Int>(0)
    val safeAppsCount: StateFlow<Int> = _safeAppsCount.asStateFlow()

    private val _lowRiskAppsCount = MutableStateFlow<Int>(0)
    val lowRiskAppsCount: StateFlow<Int> = _lowRiskAppsCount.asStateFlow()

    private val _mediumRiskAppsCount = MutableStateFlow<Int>(0)
    val mediumRiskAppsCount: StateFlow<Int> = _mediumRiskAppsCount.asStateFlow()

    private val _highRiskAppsCount = MutableStateFlow<Int>(0)
    val highRiskAppsCount: StateFlow<Int> = _highRiskAppsCount.asStateFlow()

    private val _criticalAppsCount = MutableStateFlow<Int>(0)
    val criticalAppsCount: StateFlow<Int> = _criticalAppsCount.asStateFlow()

    // --- New Specific Flows for SuspiciousApps ---
    private val _suspiciousAppByPackage = MutableStateFlow<SuspiciousApp?>(null)
    val suspiciousAppByPackage: StateFlow<SuspiciousApp?> = _suspiciousAppByPackage.asStateFlow()

    private val _notSentSuspiciousApps = MutableStateFlow<List<SuspiciousApp>>(emptyList())
    val notSentSuspiciousApps: StateFlow<List<SuspiciousApp>> = _notSentSuspiciousApps.asStateFlow()

    private val _recommendedSuspicious = MutableStateFlow<List<SuspiciousApp>>(emptyList())
    val recommendedSuspicious: StateFlow<List<SuspiciousApp>> = _recommendedSuspicious.asStateFlow()

    private val _suspiciousTotalCount = MutableStateFlow<Int>(0)
    val suspiciousTotalCount: StateFlow<Int> = _suspiciousTotalCount.asStateFlow()

    private val _recommendedSuspiciousCount = MutableStateFlow<Int>(0)
    val recommendedSuspiciousCount: StateFlow<Int> = _recommendedSuspiciousCount.asStateFlow()

    private val _notSentSuspiciousCount = MutableStateFlow<Int>(0)
    val notSentSuspiciousCount: StateFlow<Int> = _notSentSuspiciousCount.asStateFlow()

    private val _sentSuspiciousCount = MutableStateFlow<Int>(0)
    val sentSuspiciousCount: StateFlow<Int> = _sentSuspiciousCount.asStateFlow()


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

    fun getAppByPackage(pkg: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getAppByPackage(pkg)
        _appByPackage.value = result
    }

    fun getAppBySha256(hash: String,) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getAppBySha256(hash)
        _appBySha256.value = result
    }

    fun getAppByMd5(md5: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getAppByMd5(md5)
        _appByMd5.value = result
    }

    fun getAppBySha1(sha1: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getAppBySha1(sha1)
        _appBySha1.value = result
    }

    fun countApps(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countApps()
        _appsCount.value = result
        onResult(result)
    }

    fun getSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        _suspiciousApps.value = appDataBaseRepo.getSuspiciousApps()
    }

    fun getSafeApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getSafeApps()
        _allApps.value = result
        _safeApps.value = result
    }

    fun getLowRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getLowRiskApps()
        _allApps.value = result
        _lowRiskApps.value = result
    }

    fun getMediumRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getMediumRiskApps()
        _allApps.value = result
        _mediumRiskApps.value = result
    }

    fun getHighRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getHighRiskApps()
        _allApps.value = result
        _highRiskApps.value = result
    }

    fun getCriticalApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getCriticalApps()
        _allApps.value = result
        _criticalApps.value = result
    }

    fun getAppsByRisk() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getAppsByRisk()
        _allApps.value = result
        _appsByRisk.value = result
    }

    fun getNewestApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getNewestApps()
        _allApps.value = result
        _newestApps.value = result
    }

    fun getRecentlyUpdatedApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getRecentlyUpdatedApps()
        _allApps.value = result
        _recentlyUpdatedApps.value = result
    }

    fun getAppsByInstaller(installer: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getAppsByInstaller(installer)
        _allApps.value = result
        _appsByInstaller.value = result
    }

    fun getDebuggableApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getDebuggableApps()
        _allApps.value = result
        _debuggableApps.value = result
    }

    fun getDisabledApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getDisabledApps()
        _allApps.value = result
        _disabledApps.value = result
    }

    fun getOldTargetSdkApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getOldTargetSdkApps()
        _allApps.value = result
        _oldTargetSdkApps.value = result
    }

    fun getAppsByCertificateSha256(sha256: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getAppsByCertificateSha256(sha256)
        _allApps.value = result
        _appsByCertificateSha256.value = result
    }

    fun searchApps(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.searchApps(keyword)
        _allApps.value = result
        _searchResultApps.value = result
    }

    fun getLargestApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getLargestApps()
        _allApps.value = result
        _largestApps.value = result
    }

    fun getSmallestApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getSmallestApps()
        _allApps.value = result
        _smallestApps.value = result
    }

    fun getRecommendedForUpload() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getRecommendedForUpload()
        _allApps.value = result
        _recommendedForUpload.value = result
    }

    fun countRecommendedUploads(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countRecommendedUploads()
        _recommendedUploadsCount.value = result
        onResult(result)
    }

    fun getRecommendedSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getRecommendedSuspiciousApps()
        _suspiciousApps.value = result
        _recommendedSuspiciousApps.value = result
    }

    fun countSystemApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countSystemApps()
        _systemAppsCount.value = result
    }

    fun countUserApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countUserApps()
        _userAppsCount.value = result
    }

    fun countSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countSuspiciousApps()
        _suspiciousAppsCount.value = result
    }

    fun countSafeApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countSafeApps()
        _safeAppsCount.value = result
    }

    fun countLowRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countLowRiskApps()
        _lowRiskAppsCount.value = result
    }

    fun countMediumRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countMediumRiskApps()
        _mediumRiskAppsCount.value = result
    }

    fun countHighRiskApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countHighRiskApps()
        _highRiskAppsCount.value = result
    }

    fun countCriticalApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countCriticalApps()
        _criticalAppsCount.value = result
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
        val result = appDataBaseRepo.getSuspiciousAppByPackage(pkg)
        _suspiciousAppByPackage.value = result
        onResult(result)
    }

    fun getNotSentSuspiciousApps() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getNotSentSuspiciousApps()
        _allSuspiciousApps.value = result
        _notSentSuspiciousApps.value = result
    }

    fun getRecommendedSuspicious() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.getRecommendedSuspicious()
        _allSuspiciousApps.value = result
        _recommendedSuspicious.value = result
    }

    fun countSuspiciousTotal(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countSuspiciousTotal()
        _suspiciousTotalCount.value = result
        onResult(result)
    }

    fun countRecommendedSuspicious(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countRecommendedSuspicious()
        _recommendedSuspiciousCount.value = result
        onResult(result)
    }

    fun countNotSentSuspicious(onResult: (Int) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countNotSentSuspicious()
        _notSentSuspiciousCount.value = result
        onResult(result)
    }

    fun countSentSuspicious() = viewModelScope.launch(Dispatchers.IO) {
        val result = appDataBaseRepo.countSentSuspicious()
        _sentSuspiciousCount.value = result
    }

    fun markSuspiciousReportSent(pkg: String, date: String) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.markSuspiciousReportSent(pkg, date)
    }

    fun markApkUploaded(pkg: String, date: String) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.markApkUploaded(pkg, date)
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
