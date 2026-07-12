package com.example.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hashscanner.data.model.db_entities.AppInfo

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: AppInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<AppInfo>)

    @Update
    suspend fun update(app: AppInfo)

    @Delete
    suspend fun delete(app: AppInfo)

    @Query("DELETE FROM apps")
    suspend fun deleteAll()

    @Query("SELECT * FROM apps ORDER BY appName ASC")
    suspend fun getAll(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE packageName=:pkg LIMIT 1")
    suspend fun getByPackage(pkg: String): AppInfo?

    @Query("SELECT * FROM apps WHERE sha256=:hash LIMIT 1")
    suspend fun getBySha256(hash: String): AppInfo?

    @Query("SELECT * FROM apps WHERE md5=:md5 LIMIT 1")
    suspend fun getByMd5(md5: String): AppInfo?

    @Query("SELECT * FROM apps WHERE sha1=:sha1 LIMIT 1")
    suspend fun getBySha1(sha1: String): AppInfo?

    @Query("SELECT COUNT(*) FROM apps")
    suspend fun count(): Int

    @Query("SELECT * FROM apps WHERE suspicious=1")
    suspend fun getSuspicious(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE riskLevel='SAFE'")
    suspend fun getSafeApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE riskLevel='LOW'")
    suspend fun getLowRiskApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE riskLevel='MEDIUM'")
    suspend fun getMediumRiskApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE riskLevel='HIGH'")
    suspend fun getHighRiskApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE riskLevel='CRITICAL'")
    suspend fun getCriticalApps(): List<AppInfo>

    @Query("SELECT * FROM apps ORDER BY riskScore DESC")
    suspend fun getAppsByRisk(): List<AppInfo>

    @Query("SELECT * FROM apps ORDER BY firstInstallTime DESC")
    suspend fun getNewestApps(): List<AppInfo>

    @Query("SELECT * FROM apps ORDER BY lastUpdateTime DESC")
    suspend fun getRecentlyUpdatedApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE installer=:installer")
    suspend fun getAppsByInstaller(installer: String): List<AppInfo>

    @Query("SELECT * FROM apps WHERE isDebuggable=1")
    suspend fun getDebuggableApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE isEnabled=0")
    suspend fun getDisabledApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE targetSdk<30")
    suspend fun getOldTargetSdkApps(): List<AppInfo>

    @Query("SELECT * FROM apps WHERE certificateSha256=:sha256")
    suspend fun getByCertificateSha256(sha256: String): List<AppInfo>

    @Query("SELECT * FROM apps WHERE packageName LIKE '%' || :keyword || '%' OR appName LIKE '%' || :keyword || '%'")
    suspend fun search(keyword: String): List<AppInfo>

    @Query("SELECT * FROM apps ORDER BY apkSize DESC")
    suspend fun getLargestApps(): List<AppInfo>

    @Query("SELECT * FROM apps ORDER BY apkSize ASC")
    suspend fun getSmallestApps(): List<AppInfo>

    // ---------- Upload Recommendation ----------

    @Query("""
        SELECT * FROM apps
        WHERE recommendUpload = 1
        ORDER BY riskScore DESC
    """)
    suspend fun getRecommendedForUpload(): List<AppInfo>

    @Query("""
        SELECT COUNT(*)
        FROM apps
        WHERE recommendUpload = 1
    """)
    suspend fun countRecommendedUploads(): Int

    @Query("""
        SELECT * FROM apps
        WHERE recommendUpload = 1
        AND suspicious = 1
        ORDER BY riskScore DESC
    """)
    suspend fun getRecommendedSuspiciousApps(): List<AppInfo>

    // ---------- Statistics ----------

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=1")
    suspend fun countSystemApps(): Int

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=0")
    suspend fun countUserApps(): Int

    @Query("SELECT COUNT(*) FROM apps WHERE suspicious=1")
    suspend fun countSuspiciousApps(): Int

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='SAFE'")
    suspend fun countSafeApps(): Int

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='LOW'")
    suspend fun countLowRiskApps(): Int

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='MEDIUM'")
    suspend fun countMediumRiskApps(): Int

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='HIGH'")
    suspend fun countHighRiskApps(): Int

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='CRITICAL'")
    suspend fun countCriticalApps(): Int

}