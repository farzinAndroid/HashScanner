package com.example.hashscanner.data.report

import com.example.hashscanner.data.database.AppDatabase
import com.example.hashscanner.data.model.ScanReport
import com.example.hashscanner.utils.DateTimeUtils

class ReportGenerator(

    private val db: AppDatabase

) {

    suspend fun generate(): ScanReport {

        val apps = db.appDao().getAll()

        val total = apps.size

        val system = apps.count {

            it.isSystem

        }

        val user = total - system

        val suspicious = apps.count {

            it.suspicious

        }

        val high = apps.count {

            it.riskLevel == "HIGH"

        }

        val medium = apps.count {

            it.riskLevel == "MEDIUM"

        }

        val low = apps.count {

            it.riskLevel == "LOW"

        }

        val safe = apps.count {

            it.riskLevel == "SAFE"

        }

        return ScanReport(

            totalApps = total,

            systemApps = system,

            userApps = user,

            suspiciousApps = suspicious,

            highRiskApps = high,

            mediumRiskApps = medium,

            lowRiskApps = low,

            safeApps = safe,

            scanDate = DateTimeUtils.getCurrentDate(),

            scanTime = DateTimeUtils.getCurrentTime()

        )

    }

}