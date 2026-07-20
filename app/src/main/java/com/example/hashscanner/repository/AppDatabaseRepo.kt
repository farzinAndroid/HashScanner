package com.example.hashscanner.repository

import com.example.hashscanner.data.database.dao.AppDao
import com.example.hashscanner.data.database.dao.PermissionDao
import com.example.hashscanner.data.database.dao.ScanHistoryDao
import com.example.hashscanner.data.database.dao.SuspiciousDao
import com.example.hashscanner.data.model.db_entities.AppInfo
import com.example.hashscanner.data.model.db_entities.PermissionInfo
import com.example.hashscanner.data.model.db_entities.ScanHistory
import com.example.hashscanner.data.model.db_entities.SuspiciousApp
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
    suspend fun getAllApps() = appDao.getAll()
    suspend fun getAppByPackage(pkg: String) = appDao.getByPackage(pkg)
    suspend fun getAppBySha256(hash: String) = appDao.getBySha256(hash)
    suspend fun getAppByMd5(md5: String) = appDao.getByMd5(md5)
    suspend fun getAppBySha1(sha1: String) = appDao.getBySha1(sha1)
    suspend fun countApps() = appDao.count()
    suspend fun getSuspiciousApps() = appDao.getSuspicious()
    suspend fun getSafeApps() = appDao.getSafeApps()
    suspend fun getLowRiskApps() = appDao.getLowRiskApps()
    suspend fun getMediumRiskApps() = appDao.getMediumRiskApps()
    suspend fun getHighRiskApps() = appDao.getHighRiskApps()
    suspend fun getCriticalApps() = appDao.getCriticalApps()
    suspend fun getAppsByRisk() = appDao.getAppsByRisk()
    suspend fun getNewestApps() = appDao.getNewestApps()
    suspend fun getRecentlyUpdatedApps() = appDao.getRecentlyUpdatedApps()
    suspend fun getAppsByInstaller(installer: String) = appDao.getAppsByInstaller(installer)
    suspend fun getDebuggableApps() = appDao.getDebuggableApps()
    suspend fun getDisabledApps() = appDao.getDisabledApps()
    suspend fun getOldTargetSdkApps() = appDao.getOldTargetSdkApps()
    suspend fun getAppsByCertificateSha256(sha256: String) = appDao.getByCertificateSha256(sha256)
    suspend fun searchApps(keyword: String) = appDao.search(keyword)
    suspend fun getLargestApps() = appDao.getLargestApps()
    suspend fun getSmallestApps() = appDao.getSmallestApps()
    suspend fun getRecommendedForUpload() = appDao.getRecommendedForUpload()
    suspend fun countRecommendedUploads() = appDao.countRecommendedUploads()
    suspend fun getRecommendedSuspiciousApps() = appDao.getRecommendedSuspiciousApps()
    suspend fun countSystemApps() = appDao.countSystemApps()
    suspend fun countUserApps() = appDao.countUserApps()
    suspend fun countSuspiciousApps() = appDao.countSuspiciousApps()
    suspend fun countSafeApps() = appDao.countSafeApps()
    suspend fun countLowRiskApps() = appDao.countLowRiskApps()
    suspend fun countMediumRiskApps() = appDao.countMediumRiskApps()
    suspend fun countHighRiskApps() = appDao.countHighRiskApps()
    suspend fun countCriticalApps() = appDao.countCriticalApps()

    // PermissionDao Functions
    suspend fun insertPermission(permission: PermissionInfo) = permissionDao.insert(permission)
    suspend fun insertAllPermissions(list: List<PermissionInfo>) = permissionDao.insertAll(list)
    suspend fun getPermissions(pkg: String) = permissionDao.getPermissions(pkg)
    suspend fun deleteAllPermissions() = permissionDao.deleteAll()

    // SuspiciousDao Functions
    suspend fun insertSuspiciousApp(app: SuspiciousApp) = suspiciousDao.insert(app)
    suspend fun insertAllSuspiciousApps(apps: List<SuspiciousApp>) = suspiciousDao.insertAll(apps)
    suspend fun updateSuspiciousApp(app: SuspiciousApp) = suspiciousDao.update(app)
    suspend fun deleteSuspiciousApp(app: SuspiciousApp) = suspiciousDao.delete(app)
    suspend fun deleteAllSuspiciousApps() = suspiciousDao.deleteAll()
    suspend fun getAllSuspiciousApps() = suspiciousDao.getAll()
    suspend fun getSuspiciousAppByPackage(pkg: String) = suspiciousDao.getByPackage(pkg)
    suspend fun getNotSentSuspiciousApps() = suspiciousDao.getNotSent()
    suspend fun getRecommendedSuspicious() = suspiciousDao.getRecommended()
    suspend fun countSuspiciousTotal() = suspiciousDao.count()
    suspend fun countRecommendedSuspicious() = suspiciousDao.countRecommended()
    suspend fun countNotSentSuspicious() = suspiciousDao.countNotSent()

    suspend fun countSentSuspicious() = suspiciousDao.countSent()
    suspend fun markSuspiciousReportSent(pkg: String, date: String) = suspiciousDao.markReportSent(pkg, date)

    suspend fun markSuspiciousApkUploaded(pkg: String, date: String) = suspiciousDao.markApkUploaded(pkg, date)

    // ScanHistoryDao Functions
    suspend fun insertScanHistory(history: ScanHistory) = scanHistoryDao.insert(history)
    suspend fun getAllScanHistory() = scanHistoryDao.getAll()
    suspend fun getLastScan() = scanHistoryDao.getLastScan()
    suspend fun deleteAllScanHistory() = scanHistoryDao.deleteAll()
}