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
    
    val allApps = appDao.getAll()
    fun getAppByPackage(pkg: String) = appDao.getByPackage(pkg)
    fun getAppBySha256(hash: String) = appDao.getBySha256(hash)
    fun getAppByMd5(md5: String) = appDao.getByMd5(md5)
    fun getAppBySha1(sha1: String) = appDao.getBySha1(sha1)
    
    val appsCount = appDao.count()
    val suspiciousApps = appDao.getSuspicious()
    
    fun getSafeApps(onlyUser: Boolean = false) = appDao.getSafeApps(onlyUser)
    fun getLowRiskApps(onlyUser: Boolean = false) = appDao.getLowRiskApps(onlyUser)
    fun getMediumRiskApps(onlyUser: Boolean = false) = appDao.getMediumRiskApps(onlyUser)
    fun getHighRiskApps(onlyUser: Boolean = false) = appDao.getHighRiskApps(onlyUser)
    fun getCriticalApps(onlyUser: Boolean = false) = appDao.getCriticalApps(onlyUser)
    
    val appsByRisk = appDao.getAppsByRisk()
    
    fun getAppsByRiskAndDate(
        onlyUser: Boolean = false,
        riskLevel: String,
        scanDate: String,
        scanTime: String
    ) = appDao.getAppsByRiskAndDate(onlyUser, riskLevel, scanDate, scanTime)
    
    val newestApps = appDao.getNewestApps()
    val recentlyUpdatedApps = appDao.getRecentlyUpdatedApps()
    fun getAppsByInstaller(installer: String) = appDao.getAppsByInstaller(installer)
    val debuggableApps = appDao.getDebuggableApps()
    val disabledApps = appDao.getDisabledApps()
    val oldTargetSdkApps = appDao.getOldTargetSdkApps()
    fun getAppsByCertificateSha256(sha256: String) = appDao.getByCertificateSha256(sha256)
    fun searchApps(keyword: String) = appDao.search(keyword)
    val largestApps = appDao.getLargestApps()
    val smallestApps = appDao.getSmallestApps()
    val recommendedForUpload = appDao.getRecommendedForUpload()
    val recommendedUploadsCount = appDao.countRecommendedUploads()
    val recommendedSuspiciousApps = appDao.getRecommendedSuspiciousApps()
    
    val systemAppsCount = appDao.countSystemApps()
    val userAppsCount = appDao.countUserApps()
    val suspiciousAppsCount = appDao.countSuspiciousApps()
    
    fun countSafeApps(onlyUser: Boolean = false) = appDao.countSafeApps(onlyUser)
    fun countLowRiskApps(onlyUser: Boolean = false) = appDao.countLowRiskApps(onlyUser)
    fun countMediumRiskApps(onlyUser: Boolean = false) = appDao.countMediumRiskApps(onlyUser)
    fun countHighRiskApps(onlyUser: Boolean = false) = appDao.countHighRiskApps(onlyUser)
    fun countCriticalApps(onlyUser: Boolean = false) = appDao.countCriticalApps(onlyUser)

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
        appDao.markApkUploaded(pkg, date)
        suspiciousDao.markApkUploaded(pkg, date)
    }

    // ScanHistoryDao Functions
    suspend fun insertScanHistory(history: ScanHistory) = scanHistoryDao.insert(history)
    val allScanHistory = scanHistoryDao.getAll()
    val lastScan = scanHistoryDao.getLastScan()
    suspend fun deleteAllScanHistory() = scanHistoryDao.deleteAll()

    suspend fun deleteScanHistory(id: String) = scanHistoryDao.deleteScanHistory(id)
}