package com.example.hashscanner.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hashscanner.data.model.db_entities.AppInfo
import kotlinx.coroutines.flow.Flow

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
    fun getAll(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE packageName=:pkg LIMIT 1")
    fun getByPackage(pkg: String): Flow<AppInfo?>

    @Query("SELECT * FROM apps WHERE sha256=:hash LIMIT 1")
    fun getBySha256(hash: String): Flow<AppInfo?>

    @Query("SELECT * FROM apps WHERE md5=:md5 LIMIT 1")
    fun getByMd5(md5: String): Flow<AppInfo?>

    @Query("SELECT * FROM apps WHERE sha1=:sha1 LIMIT 1")
    fun getBySha1(sha1: String): Flow<AppInfo?>

    @Query("SELECT COUNT(*) FROM apps")
    fun count(): Flow<Int>

    @Query("SELECT * FROM apps WHERE suspicious=1")
    fun getSuspicious(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='SAFE' AND (:onlyUser = 0 OR isSystem = 0)")
    fun getSafeApps(onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='LOW' AND (:onlyUser = 0 OR isSystem = 0)")
    fun getLowRiskApps(onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='MEDIUM' AND (:onlyUser = 0 OR isSystem = 0)")
    fun getMediumRiskApps(onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='HIGH' AND (:onlyUser = 0 OR isSystem = 0)")
    fun getHighRiskApps(onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='CRITICAL' AND (:onlyUser = 0 OR isSystem = 0)")
    fun getCriticalApps(onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps ORDER BY riskScore DESC")
    fun getAppsByRisk(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel=:riskLevel AND scanDate=:scanDate AND scanTime=:scanTime AND (:onlyUser = 0 OR isSystem = 0)")
    fun getAppsByRiskAndDate(onlyUser: Boolean = false,riskLevel: String,scanDate:String,scanTime:String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps ORDER BY firstInstallTime DESC")
    fun getNewestApps(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps ORDER BY lastUpdateTime DESC")
    fun getRecentlyUpdatedApps(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE installer=:installer")
    fun getAppsByInstaller(installer: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE isDebuggable=1")
    fun getDebuggableApps(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE isEnabled=0")
    fun getDisabledApps(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE targetSdk<30")
    fun getOldTargetSdkApps(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE certificateSha256=:sha256")
    fun getByCertificateSha256(sha256: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE packageName LIKE '%' || :keyword || '%' OR appName LIKE '%' || :keyword || '%'")
    fun search(keyword: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps ORDER BY apkSize DESC")
    fun getLargestApps(): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps ORDER BY apkSize ASC")
    fun getSmallestApps(): Flow<List<AppInfo>>

    @Query("""
        UPDATE apps
        SET apkUploaded = 1,
            uploadDate = :date
        WHERE packageName = :pkg
    """)
    suspend fun markApkUploaded(
        pkg: String,
        date: String
    )

    // ---------- Upload Recommendation ----------

    @Query("""
        SELECT * FROM apps
        WHERE recommendUpload = 1
        ORDER BY riskScore DESC
    """)
    fun getRecommendedForUpload(): Flow<List<AppInfo>>

    @Query("""
        SELECT COUNT(*)
        FROM apps
        WHERE recommendUpload = 1
    """)
    fun countRecommendedUploads(): Flow<Int>

    @Query("""
        SELECT * FROM apps
        WHERE recommendUpload = 1
        AND suspicious = 1
        ORDER BY riskScore DESC
    """)
    fun getRecommendedSuspiciousApps(): Flow<List<AppInfo>>

    // ---------- Statistics ----------

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=1")
    fun countSystemApps(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=0")
    fun countUserApps(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE suspicious=1")
    fun countSuspiciousApps(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='SAFE' AND (:onlyUser = 0 OR isSystem = 0)")
    fun countSafeApps(onlyUser: Boolean = false): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='LOW' AND (:onlyUser = 0 OR isSystem = 0)")
    fun countLowRiskApps(onlyUser: Boolean = false): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='MEDIUM' AND (:onlyUser = 0 OR isSystem = 0)")
    fun countMediumRiskApps(onlyUser: Boolean = false): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='HIGH' AND (:onlyUser = 0 OR isSystem = 0)")
    fun countHighRiskApps(onlyUser: Boolean = false): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel='CRITICAL' AND (:onlyUser = 0 OR isSystem = 0)")
    fun countCriticalApps(onlyUser: Boolean = false): Flow<Int>

}