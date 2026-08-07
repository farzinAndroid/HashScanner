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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDatabaseViewModel @Inject constructor(
    private val appDataBaseRepo: AppDatabaseRepo
) : ViewModel() {

    // --- State Sources (Internal) ---
    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps = _allApps.asStateFlow()

    private val _allPermissions = MutableStateFlow<List<PermissionInfo>>(emptyList())
    val allPermissions = _allPermissions.asStateFlow()

    private val _appByPackage = MutableStateFlow<AppInfo?>(null)
    val appByPackage = _appByPackage.asStateFlow()

    private val _appBySha256 = MutableStateFlow<AppInfo?>(null)
    val appBySha256 = _appBySha256.asStateFlow()

    private val _appByMd5 = MutableStateFlow<AppInfo?>(null)
    val appByMd5 = _appByMd5.asStateFlow()

    private val _appBySha1 = MutableStateFlow<AppInfo?>(null)
    val appBySha1 = _appBySha1.asStateFlow()

    private val _appsByCertificateSha256 = MutableStateFlow<List<AppInfo>>(emptyList())
    val appsByCertificateSha256 = _appsByCertificateSha256.asStateFlow()

    private val _searchResultApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val searchResultApps = _searchResultApps.asStateFlow()

    private val _suspiciousAppByPackage = MutableStateFlow<SuspiciousApp?>(null)
    val suspiciousAppByPackage = _suspiciousAppByPackage.asStateFlow()

    private val _appsByRiskAndDate = MutableStateFlow<List<AppInfo>>(emptyList())
    val appsByRiskAndDate = _appsByRiskAndDate.asStateFlow()

    private val _safeAppsCount = MutableStateFlow(0)
    val safeAppsCount = _safeAppsCount.asStateFlow()

    private val _lowRiskAppsCount = MutableStateFlow(0)
    val lowRiskAppsCount = _lowRiskAppsCount.asStateFlow()

    private val _mediumRiskAppsCount = MutableStateFlow(0)
    val mediumRiskAppsCount = _mediumRiskAppsCount.asStateFlow()

    private val _highRiskAppsCount = MutableStateFlow(0)
    val highRiskAppsCount = _highRiskAppsCount.asStateFlow()

    private val _criticalAppsCount = MutableStateFlow(0)
    val criticalAppsCount = _criticalAppsCount.asStateFlow()

    private val _suspiciousTotalCount = MutableStateFlow(0)
    val suspiciousTotalCount = _suspiciousTotalCount.asStateFlow()

    private val _recommendedSuspiciousCount = MutableStateFlow(0)
    val recommendedSuspiciousCount = _recommendedSuspiciousCount.asStateFlow()

    private val _notSentSuspiciousCount = MutableStateFlow(0)
    val notSentSuspiciousCount = _notSentSuspiciousCount.asStateFlow()

    private val _sentSuspiciousCount = MutableStateFlow(0)
    val sentSuspiciousCount = _sentSuspiciousCount.asStateFlow()

    // --- Direct Exposure Flows (from Repo) ---
    val suspiciousApps = appDataBaseRepo.suspiciousApps
    val scanHistory = appDataBaseRepo.allScanHistory
    val lastScan = appDataBaseRepo.lastScan
    val allSuspiciousApps = appDataBaseRepo.allSuspiciousApps
    val safeApps = appDataBaseRepo.getSafeApps()
    val lowRiskApps = appDataBaseRepo.getLowRiskApps()
    val mediumRiskApps = appDataBaseRepo.getMediumRiskApps()
    val highRiskApps = appDataBaseRepo.getHighRiskApps()
    val criticalApps = appDataBaseRepo.getCriticalApps()
    val appsByRisk = appDataBaseRepo.appsByRisk
    val newestApps = appDataBaseRepo.newestApps
    val recentlyUpdatedApps = appDataBaseRepo.recentlyUpdatedApps
    val debuggableApps = appDataBaseRepo.debuggableApps
    val disabledApps = appDataBaseRepo.disabledApps
    val oldTargetSdkApps = appDataBaseRepo.oldTargetSdkApps
    val largestApps = appDataBaseRepo.largestApps
    val smallestApps = appDataBaseRepo.smallestApps
    val recommendedForUpload = appDataBaseRepo.recommendedForUpload
    val recommendedUploadsCount = appDataBaseRepo.recommendedUploadsCount
    val recommendedSuspiciousApps = appDataBaseRepo.recommendedSuspiciousApps
    val appsCount = appDataBaseRepo.appsCount
    val recommendedUploadsCountFromRepo = appDataBaseRepo.recommendedUploadsCount
    val systemAppsCount = appDataBaseRepo.systemAppsCount
    val userAppsCount = appDataBaseRepo.userAppsCount
    val suspiciousAppsCount = appDataBaseRepo.suspiciousAppsCount
    val notSentSuspiciousApps = appDataBaseRepo.notSentSuspiciousApps
    val recommendedSuspicious = appDataBaseRepo.recommendedSuspicious


    fun getAllApps() {
        viewModelScope.launch {
            appDataBaseRepo.allApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

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

    fun getAppByPackage(pkg: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppByPackage(pkg).collectLatest {
                _appByPackage.emit(it)
            }
        }
    }

    fun getAppBySha256(hash: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppBySha256(hash).collectLatest {
                _appBySha256.emit(it)
            }
        }
    }

    fun getAppByMd5(md5: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppByMd5(md5).collectLatest {
                _appByMd5.emit(it)
            }
        }
    }

    fun getAppBySha1(sha1: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppBySha1(sha1).collectLatest {
                _appBySha1.emit(it)
            }
        }
    }

    fun countApps(onResult: (Int) -> Unit) {
        viewModelScope.launch {
            appDataBaseRepo.appsCount.collectLatest {
                onResult(it)
            }
        }
    }

    fun countSafeApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countSafeApps(onlyUser).collectLatest {
                _safeAppsCount.emit(it)
            }
        }
    }

    fun countLowRiskApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countLowRiskApps(onlyUser).collectLatest {
                _lowRiskAppsCount.emit(it)
            }
        }
    }

    fun countMediumRiskApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countMediumRiskApps(onlyUser).collectLatest {
                _mediumRiskAppsCount.emit(it)
            }
        }
    }

    fun countHighRiskApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countHighRiskApps(onlyUser).collectLatest {
                _highRiskAppsCount.emit(it)
            }
        }
    }

    fun countCriticalApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countCriticalApps(onlyUser).collectLatest {
                _criticalAppsCount.emit(it)
            }
        }
    }

    fun getSuspiciousApps() {
        viewModelScope.launch {
            appDataBaseRepo.suspiciousApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getSafeApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getSafeApps(onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getLowRiskApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getLowRiskApps(onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getMediumRiskApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getMediumRiskApps(onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getHighRiskApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getHighRiskApps(onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getCriticalApps(onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getCriticalApps(onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByRisk() {
        viewModelScope.launch {
            appDataBaseRepo.appsByRisk.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getNewestApps() {
        viewModelScope.launch {
            appDataBaseRepo.newestApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getRecentlyUpdatedApps() {
        viewModelScope.launch {
            appDataBaseRepo.recentlyUpdatedApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByInstaller(installer: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppsByInstaller(installer).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getDebuggableApps() {
        viewModelScope.launch {
            appDataBaseRepo.debuggableApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getDisabledApps() {
        viewModelScope.launch {
            appDataBaseRepo.disabledApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getOldTargetSdkApps() {
        viewModelScope.launch {
            appDataBaseRepo.oldTargetSdkApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByCertificateSha256(sha256: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppsByCertificateSha256(sha256).collectLatest {
                _appsByCertificateSha256.emit(it)
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByRiskAndDate(
        onlyUser: Boolean = false,
        riskLevel: String,
        scanDate: String,
        scanTime: String
    ) {
        viewModelScope.launch {
            appDataBaseRepo.getAppsByRiskAndDate(onlyUser, riskLevel, scanDate, scanTime).collectLatest {
                _appsByRiskAndDate.emit(it)
                _allApps.emit(it)
            }
        }
    }

    fun searchApps(keyword: String) {
        viewModelScope.launch {
            appDataBaseRepo.searchApps(keyword).collectLatest {
                _searchResultApps.emit(it)
                _allApps.emit(it)
            }
        }
    }

    fun getLargestApps() {
        viewModelScope.launch {
            appDataBaseRepo.largestApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getSmallestApps() {
        viewModelScope.launch {
            appDataBaseRepo.smallestApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getRecommendedForUpload() {
        viewModelScope.launch {
            appDataBaseRepo.recommendedForUpload.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun countRecommendedUploads(onResult: (Int) -> Unit) {
        viewModelScope.launch {
            appDataBaseRepo.recommendedUploadsCount.collectLatest {
                onResult(it)
            }
        }
    }

    fun getRecommendedSuspiciousApps() {
        viewModelScope.launch {
            appDataBaseRepo.recommendedSuspiciousApps.collectLatest {
                _allApps.emit(it)
            }
        }
    }

    // --- PermissionDao Functions ---

    fun insertPermission(permission: PermissionInfo) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertPermission(permission)
    }

    fun insertAllPermissions(list: List<PermissionInfo>) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.insertAllPermissions(list)
    }

    fun getPermissions(pkg: String) {
        viewModelScope.launch {
            appDataBaseRepo.getPermissions(pkg).collectLatest {
                _allPermissions.emit(it)
            }
        }
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

    fun getSuspiciousAppByPackage(pkg: String, onResult: (SuspiciousApp?) -> Unit) {
        viewModelScope.launch {
            appDataBaseRepo.getSuspiciousAppByPackage(pkg).collectLatest {
                _suspiciousAppByPackage.emit(it)
                onResult(it)
            }
        }
    }

    fun countSuspiciousTotal() {
        viewModelScope.launch {
            appDataBaseRepo.suspiciousTotalCount.collectLatest {
                _suspiciousTotalCount.emit(it)
            }
        }
    }

    fun countRecommendedSuspicious() {
        viewModelScope.launch {
            appDataBaseRepo.recommendedSuspiciousCount.collectLatest {
                _recommendedSuspiciousCount.emit(it)
            }
        }
    }

    fun countNotSentSuspicious() {
        viewModelScope.launch {
            appDataBaseRepo.notSentSuspiciousCount.collectLatest {
                _notSentSuspiciousCount.emit(it)
            }
        }
    }

    fun countSentSuspicious() {
        viewModelScope.launch {
            appDataBaseRepo.sentSuspiciousCount.collectLatest {
                _sentSuspiciousCount.emit(it)
            }
        }
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

    fun deleteAllScanHistory() = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteAllScanHistory()
    }

    fun deleteScanHistory(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteScanHistory(id)
    }
}
