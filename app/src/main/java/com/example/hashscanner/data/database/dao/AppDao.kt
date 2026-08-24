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

    @Query("SELECT * FROM apps WHERE scanId = :scanId ORDER BY appName ASC")
    fun getAllByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE packageName=:pkg AND scanId = :scanId LIMIT 1")
    fun getByPackageAndScanId(pkg: String, scanId: String): Flow<AppInfo?>

    @Query("SELECT * FROM apps WHERE sha256=:hash AND scanId = :scanId LIMIT 1")
    fun getBySha256ByScanId(hash: String, scanId: String): Flow<AppInfo?>

    @Query("SELECT * FROM apps WHERE md5=:md5 AND scanId = :scanId LIMIT 1")
    fun getByMd5ByScanId(md5: String, scanId: String): Flow<AppInfo?>

    @Query("SELECT * FROM apps WHERE sha1=:sha1 AND scanId = :scanId LIMIT 1")
    fun getBySha1ByScanId(sha1: String, scanId: String): Flow<AppInfo?>

    @Query("SELECT COUNT(*) FROM apps WHERE scanId = :scanId")
    fun countByScanId(scanId: String): Flow<Int>

    @Query("SELECT * FROM apps WHERE suspicious=1 AND scanId = :scanId")
    fun getSuspiciousByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='SAFE' AND scanId = :scanId AND (:onlyUser = 0 OR isSystem = 0)")
    fun getSafeAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='LOW' AND scanId = :scanId AND (:onlyUser = 0 OR isSystem = 0)")
    fun getLowRiskAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='MEDIUM' AND scanId = :scanId AND (:onlyUser = 0 OR isSystem = 0)")
    fun getMediumRiskAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='HIGH' AND scanId = :scanId AND (:onlyUser = 0 OR isSystem = 0)")
    fun getHighRiskAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE riskLevel='CRITICAL' AND scanId = :scanId AND (:onlyUser = 0 OR isSystem = 0)")
    fun getCriticalAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE scanId = :scanId ORDER BY riskScore DESC")
    fun getAppsByRiskByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("""
        SELECT * FROM apps 
        WHERE scanId = :scanId 
        AND (:onlyUser = 0 OR isSystem = 0)
        AND (
            (isServerVerified = 1 AND serverResult = :riskLevel) OR 
            (isServerVerified = 0 AND riskLevel = :riskLevel) OR
            (isServerVerified = 1 AND :riskLevel = 'CRITICAL' AND serverResult = 'VIRUS') OR
            (isServerVerified = 1 AND :riskLevel = 'HIGH' AND serverResult = 'SUSPICIOUS')
        )
    """)
    fun getAppsByRiskAndScanId(onlyUser: Boolean = false, riskLevel: String, scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE scanId = :scanId ORDER BY firstInstallTime DESC")
    fun getNewestAppsByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE scanId = :scanId ORDER BY lastUpdateTime DESC")
    fun getRecentlyUpdatedAppsByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE installer=:installer AND scanId = :scanId")
    fun getAppsByInstallerByScanId(scanId: String, installer: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE isDebuggable=1 AND scanId = :scanId")
    fun getDebuggableAppsByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE isEnabled=0 AND scanId = :scanId")
    fun getDisabledAppsByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE targetSdk<30 AND scanId = :scanId")
    fun getOldTargetSdkAppsByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE certificateSha256=:sha256 AND scanId = :scanId")
    fun getByCertificateSha256ByScanId(scanId: String, sha256: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE (packageName LIKE '%' || :keyword || '%' OR appName LIKE '%' || :keyword || '%') AND scanId = :scanId")
    fun searchByScanId(scanId: String, keyword: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE scanId = :scanId ORDER BY apkSize DESC")
    fun getLargestAppsByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("SELECT * FROM apps WHERE scanId = :scanId ORDER BY apkSize ASC")
    fun getSmallestAppsByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("""
        UPDATE apps 
        SET apkUploaded = 1, 
            uploadDate = :date 
        WHERE sha256 = :hash
    """)
    suspend fun markApkUploadedByHash(
        hash: String,
        date: String
    )

    @Query("UPDATE apps SET isDeleted = 1 WHERE packageName = :pkg AND scanId = :scanId")
    suspend fun markAsDeleted(pkg: String, scanId: String)

    @Query("""
        UPDATE apps 
        SET isServerVerified = 1, 
            serverResult = :result, 
            serverAction = :action
        WHERE packageName = :pkg AND scanId = :scanId
    """)
    suspend fun updateAppVerdict(
        pkg: String, 
        scanId: String, 
        result: String, 
        action: String
    )

    // ---------- Upload Recommendation ----------

    @Query("""
        SELECT * FROM apps 
        WHERE recommendUpload = 1 
        AND scanId = :scanId
        ORDER BY riskScore DESC
    """)
    fun getRecommendedForUploadByScanId(scanId: String): Flow<List<AppInfo>>

    @Query("""
        SELECT COUNT(*) 
        FROM apps 
        WHERE recommendUpload = 1
        AND scanId = :scanId
    """)
    fun countRecommendedUploadsByScanId(scanId: String): Flow<Int>

    @Query("""
        SELECT * FROM apps 
        WHERE recommendUpload = 1 
        AND suspicious = 1 
        AND scanId = :scanId
        ORDER BY riskScore DESC
    """)
    fun getRecommendedSuspiciousAppsByScanId(scanId: String): Flow<List<AppInfo>>

    // ---------- Statistics ----------

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=1 AND scanId = :scanId")
    fun countSystemAppsByScanId(scanId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=0 AND scanId = :scanId")
    fun countUserAppsByScanId(scanId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE suspicious=1 AND scanId = :scanId")
    fun countSuspiciousAppsByScanId(scanId: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM apps 
        WHERE scanId = :scanId 
        AND (:onlyUser = 0 OR isSystem = 0)
        AND (
            (isServerVerified = 1 AND serverResult = 'SAFE') OR 
            (isServerVerified = 0 AND riskLevel = 'SAFE')
        )
    """)
    fun countSafeAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM apps 
        WHERE scanId = :scanId 
        AND (:onlyUser = 0 OR isSystem = 0)
        AND (isServerVerified = 0 AND riskLevel = 'LOW')
    """)
    fun countLowRiskAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM apps 
        WHERE scanId = :scanId 
        AND (:onlyUser = 0 OR isSystem = 0)
        AND (isServerVerified = 0 AND riskLevel = 'MEDIUM')
    """)
    fun countMediumRiskAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM apps 
        WHERE scanId = :scanId 
        AND (:onlyUser = 0 OR isSystem = 0)
        AND (
            (isServerVerified = 1 AND serverResult = 'SUSPICIOUS') OR 
            (isServerVerified = 0 AND riskLevel = 'HIGH')
        )
    """)
    fun countHighRiskAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM apps 
        WHERE scanId = :scanId 
        AND (:onlyUser = 0 OR isSystem = 0)
        AND (
            (isServerVerified = 1 AND serverResult = 'VIRUS') OR 
            (isServerVerified = 0 AND riskLevel = 'CRITICAL')
        )
    """)
    fun countCriticalAppsByScanId(scanId: String, onlyUser: Boolean = false): Flow<Int>

}
