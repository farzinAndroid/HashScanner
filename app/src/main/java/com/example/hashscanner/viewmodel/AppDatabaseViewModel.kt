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
    private val _selectedScanId = MutableStateFlow<String?>(null)
    val selectedScanId = _selectedScanId.asStateFlow()

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

    // --- Direct Exposure Flows (from Repo) ---
    val scanHistory = appDataBaseRepo.allScanHistory
    val lastScan = appDataBaseRepo.lastScan

    // --- Direct Exposure Flows (from Repo) ---
    val allSuspiciousApps = appDataBaseRepo.allSuspiciousApps
    val notSentSuspiciousApps = appDataBaseRepo.notSentSuspiciousApps
    val recommendedSuspicious = appDataBaseRepo.recommendedSuspicious
    val suspiciousTotalCount = appDataBaseRepo.suspiciousTotalCount
    val recommendedSuspiciousCount = appDataBaseRepo.recommendedSuspiciousCount
    val notSentSuspiciousCount = appDataBaseRepo.notSentSuspiciousCount
    val sentSuspiciousCount = appDataBaseRepo.sentSuspiciousCount

    // --- State Sources (Internal) for Manual Collection ---
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


    fun getAllApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAllAppsByScanId(scanId).collectLatest {
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

    fun getAppByPackage(pkg: String, scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppByPackageAndScanId(pkg, scanId).collectLatest {
                _appByPackage.emit(it)
            }
        }
    }

    fun getAppBySha256(hash: String, scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppBySha256ByScanId(hash, scanId).collectLatest {
                _appBySha256.emit(it)
            }
        }
    }

    fun getAppByMd5(md5: String, scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppByMd5ByScanId(md5, scanId).collectLatest {
                _appByMd5.emit(it)
            }
        }
    }

    fun getAppBySha1(sha1: String, scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppBySha1ByScanId(sha1, scanId).collectLatest {
                _appBySha1.emit(it)
            }
        }
    }

    fun countApps(scanId: String, onResult: (Int) -> Unit) {
        viewModelScope.launch {
            appDataBaseRepo.countAppsByScanId(scanId).collectLatest {
                onResult(it)
            }
        }
    }

    fun countSafeApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countSafeAppsByScanId(scanId, onlyUser).collectLatest {
                _safeAppsCount.emit(it)
            }
        }
    }

    fun countLowRiskApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countLowRiskAppsByScanId(scanId, onlyUser).collectLatest {
                _lowRiskAppsCount.emit(it)
            }
        }
    }

    fun countMediumRiskApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countMediumRiskAppsByScanId(scanId, onlyUser).collectLatest {
                _mediumRiskAppsCount.emit(it)
            }
        }
    }

    fun countHighRiskApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countHighRiskAppsByScanId(scanId, onlyUser).collectLatest {
                _highRiskAppsCount.emit(it)
            }
        }
    }

    fun countCriticalApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.countCriticalAppsByScanId(scanId, onlyUser).collectLatest {
                _criticalAppsCount.emit(it)
            }
        }
    }

    fun getSuspiciousApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getSuspiciousAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getSafeApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getSafeAppsByScanId(scanId, onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getLowRiskApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getLowRiskAppsByScanId(scanId, onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getMediumRiskApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getMediumRiskAppsByScanId(scanId, onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getHighRiskApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getHighRiskAppsByScanId(scanId, onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getCriticalApps(scanId: String, onlyUser: Boolean = false) {
        viewModelScope.launch {
            appDataBaseRepo.getCriticalAppsByScanId(scanId, onlyUser).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByRisk(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppsByRiskByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getNewestApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getNewestAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getRecentlyUpdatedApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getRecentlyUpdatedAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByInstaller(installer: String, scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppsByInstallerByScanId(installer, scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getDebuggableApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getDebuggableAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getDisabledApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getDisabledAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getOldTargetSdkApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getOldTargetSdkAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByCertificateSha256(sha256: String, scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppsByCertificateSha256ByScanId(scanId, sha256).collectLatest {
                _appsByCertificateSha256.emit(it)
                _allApps.emit(it)
            }
        }
    }

    fun getAppsByRiskAndScanId(
        onlyUser: Boolean = false,
        riskLevel: String,
        scanId: String
    ) {
        viewModelScope.launch {
            appDataBaseRepo.getAppsByRiskAndScanId(onlyUser, riskLevel, scanId).collectLatest {
                _appsByRiskAndDate.emit(it)
                _allApps.emit(it)
            }
        }
    }

    fun searchApps(keyword: String, scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.searchAppsByScanId(scanId, keyword).collectLatest {
                _searchResultApps.emit(it)
                _allApps.emit(it)
            }
        }
    }

    fun getLargestApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getLargestAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getSmallestApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getSmallestAppsByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun getRecommendedForUpload(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getRecommendedForUploadByScanId(scanId).collectLatest {
                _allApps.emit(it)
            }
        }
    }

    fun countRecommendedUploads(scanId: String, onResult: (Int) -> Unit) {
        viewModelScope.launch {
            appDataBaseRepo.countRecommendedUploadsByScanId(scanId).collectLatest {
                onResult(it)
            }
        }
    }

    fun getRecommendedSuspiciousApps(scanId: String) {
        viewModelScope.launch {
            appDataBaseRepo.getRecommendedSuspiciousAppsByScanId(scanId).collectLatest {
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

    fun deleteScanHistory(id: String) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.deleteScanHistory(id)
    }

    fun markAsDeleted(pkg: String, scanId: String) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.markAsDeleted(pkg, scanId)
    }

    fun updateAnalysisStatus(scanId: String, status: String) = viewModelScope.launch(Dispatchers.IO) {
        appDataBaseRepo.updateAnalysisStatus(scanId, status)
    }
}
