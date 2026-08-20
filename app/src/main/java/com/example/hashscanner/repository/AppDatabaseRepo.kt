package com.example.hashscanner.repository

import com.example.hashscanner.data.database.dao.AppDao
import com.example.hashscanner.data.database.dao.PermissionDao
import com.example.hashscanner.data.database.dao.ScanHistoryDao
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.data.model.db_entities.PermissionInfo
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.model.db_entities.SuspiciousApp
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDatabaseRepo @Inject constructor(
    private val appDao: AppDao,
    private val permissionDao: PermissionDao,
    private val suspiciousDao: SuspiciousDao,
    private val scanHistoryDao: ScanHistoryDao
) {

    // AppDao Function
    suspend fun insertApp(app: AppInfo) = appDao.insert(app)
    suspend fun insertAllApps(apps: List<AppInfo>) = appDao.insertAll(apps)
    suspend fun updateApp(app: AppInfo) = appDao.update(app)
    suspend fun deleteApp(app: AppInfo) = appDao.delete(app)
    suspend fun deleteAllApps() = appDao.deleteAll()
    
    fun getAllAppsByScanId(scanId: String) = appDao.getAllByScanId(scanId)
    fun getAppByPackageAndScanId(pkg: String, scanId: String) = appDao.getByPackageAndScanId(pkg, scanId)
    fun getAppBySha256ByScanId(hash: String, scanId: String) = appDao.getBySha256ByScanId(hash, scanId)
    fun getAppByMd5ByScanId(md5: String, scanId: String) = appDao.getByMd5ByScanId(md5, scanId)
    fun getAppBySha1ByScanId(sha1: String, scanId: String) = appDao.getBySha1ByScanId(sha1, scanId)
    
    fun countAppsByScanId(scanId: String) = appDao.countByScanId(scanId)
    fun getSuspiciousAppsByScanId(scanId: String) = appDao.getSuspiciousByScanId(scanId)
    
    fun getSafeAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.getSafeAppsByScanId(scanId, onlyUser)
    fun getLowRiskAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.getLowRiskAppsByScanId(scanId, onlyUser)
    fun getMediumRiskAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.getMediumRiskAppsByScanId(scanId, onlyUser)
    fun getHighRiskAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.getHighRiskAppsByScanId(scanId, onlyUser)
    fun getCriticalAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.getCriticalAppsByScanId(scanId, onlyUser)
    
    fun getAppsByRiskByScanId(scanId: String) = appDao.getAppsByRiskByScanId(scanId)
    
    fun getAppsByRiskAndScanId(
        onlyUser: Boolean = false,
        riskLevel: String,
        scanId: String
    ) = appDao.getAppsByRiskAndScanId(onlyUser, riskLevel, scanId)
    
    fun getNewestAppsByScanId(scanId: String) = appDao.getNewestAppsByScanId(scanId)
    fun getRecentlyUpdatedAppsByScanId(scanId: String) = appDao.getRecentlyUpdatedAppsByScanId(scanId)
    fun getAppsByInstallerByScanId(installer: String, scanId: String) = appDao.getAppsByInstallerByScanId(installer, scanId)
    fun getDebuggableAppsByScanId(scanId: String) = appDao.getDebuggableAppsByScanId(scanId)
    fun getDisabledAppsByScanId(scanId: String) = appDao.getDisabledAppsByScanId(scanId)
    fun getOldTargetSdkAppsByScanId(scanId: String) = appDao.getOldTargetSdkAppsByScanId(scanId)
    fun getAppsByCertificateSha256ByScanId(sha256: String, scanId: String) = appDao.getByCertificateSha256ByScanId(sha256, scanId)
    fun searchAppsByScanId(keyword: String, scanId: String) = appDao.searchByScanId(keyword, scanId)
    fun getLargestAppsByScanId(scanId: String) = appDao.getLargestAppsByScanId(scanId)
    fun getSmallestAppsByScanId(scanId: String) = appDao.getSmallestAppsByScanId(scanId)
    fun getRecommendedForUploadByScanId(scanId: String) = appDao.getRecommendedForUploadByScanId(scanId)
    fun countRecommendedUploadsByScanId(scanId: String) = appDao.countRecommendedUploadsByScanId(scanId)
    fun getRecommendedSuspiciousAppsByScanId(scanId: String) = appDao.getRecommendedSuspiciousAppsByScanId(scanId)
    
    fun countSystemAppsByScanId(scanId: String) = appDao.countSystemAppsByScanId(scanId)
    fun countUserAppsByScanId(scanId: String) = appDao.countUserAppsByScanId(scanId)
    fun countSuspiciousAppsByScanId(scanId: String) = appDao.countSuspiciousAppsByScanId(scanId)
    
    fun countSafeAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.countSafeAppsByScanId(scanId, onlyUser)
    fun countLowRiskAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.countLowRiskAppsByScanId(scanId, onlyUser)
    fun countMediumRiskAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.countMediumRiskAppsByScanId(scanId, onlyUser)
    fun countHighRiskAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.countHighRiskAppsByScanId(scanId, onlyUser)
    fun countCriticalAppsByScanId(scanId: String, onlyUser: Boolean = false) = appDao.countCriticalAppsByScanId(scanId, onlyUser)

    suspend fun markAsDeleted(pkg: String, scanId: String) = appDao.markAsDeleted(pkg, scanId)

    // PermissionDao Functions
    suspend fun insertPermission(permission: PermissionInfo) = permissionDao.insert(permission)
    suspend fun insertAllPermissions(list: List<PermissionInfo>) = permissionDao.insertAll(list)
    fun getPermissions(pkg: String) = permissionDao.getPermissions(pkg)
    suspend fun deleteAllPermissions() = permissionDao.deleteAll()

    // SuspiciousDao Functions
    suspend fun insertSuspiciousApp(app: SuspiciousApp) = suspiciousDao.insert(app)
    suspend fun insertAllSuspiciousApps(apps: List<SuspiciousApp>) = suspiciousDao.insertAll(apps)
    suspend fun updateSuspiciousApp(app: SuspiciousApp) = suspiciousDao.update(app)
    suspend fun deleteSuspiciousApp(app: SuspiciousApp) = suspiciousDao.delete(app)
    suspend fun deleteAllSuspiciousApps() = suspiciousDao.deleteAll()
    val allSuspiciousApps = suspiciousDao.getAll()
    fun getSuspiciousAppByPackage(pkg: String) = suspiciousDao.getByPackage(pkg)
    val notSentSuspiciousApps = suspiciousDao.getNotSent()
    val recommendedSuspicious = suspiciousDao.getRecommended()
    val suspiciousTotalCount = suspiciousDao.count()
    val recommendedSuspiciousCount = suspiciousDao.countRecommended()
    val notSentSuspiciousCount = suspiciousDao.countNotSent()
    val sentSuspiciousCount = suspiciousDao.countSent()
    suspend fun markSuspiciousReportSent(pkg: String, date: String) = suspiciousDao.markReportSent(pkg, date)

    suspend fun markApkUploaded(pkg: String, date: String) {
        appDao.markApkUploadedGlobal(pkg, date)
        suspiciousDao.markApkUploaded(pkg, date)
    }

    // ScanHistoryDao Functions
    suspend fun insertScanHistory(history: ScanHistory) = scanHistoryDao.insert(history)
    val allScanHistory = scanHistoryDao.getAll()
    val lastScan = scanHistoryDao.getLastScan()
    suspend fun deleteAllScanHistory() = scanHistoryDao.deleteAll()

    suspend fun deleteScanHistory(id: String) = scanHistoryDao.deleteScanHistory(id)

    fun getPendingScans(pendingStatus: String) = scanHistoryDao.getPendingScans(pendingStatus)
    suspend fun updateAnalysisStatus(id: String, status: String) = scanHistoryDao.updateAnalysisStatus(id, status)
    suspend fun updateLastNotifiedStage(id: String, stage: String) = scanHistoryDao.updateLastNotifiedStage(id, stage)
    suspend fun getScanById(id: String) = scanHistoryDao.getScanById(id)
}
