package com.example.hashscanner.utils

object Constants {


    const val APP_DB_NAME = "app_hash_scanner"

    val REPORT_BASE_URL = UrlObfuscator.getReportBaseUrl()
    val APK_BASE_URL = UrlObfuscator.getApkBaseUrl()

    const val DATASTORE_NAME = "hash_scanner_datastore"


    const val UUID_DATASTORE_ID = "uuid_id"
    var UUID = ""

    // Installers
    const val INSTALLER_UNKNOWN = "Unknown"
    const val INSTALLER_PLAY_STORE = "com.android.vending"
    const val INSTALLER_BAZAAR = "com.farsitel.bazaar"
    const val INSTALLER_MYKET = "ir.mservices.market"

    val TRUSTED_INSTALLERS = listOf(
        INSTALLER_PLAY_STORE,
        INSTALLER_BAZAAR,
        INSTALLER_MYKET
    )

    // Certificate Keywords
    const val CERT_ISSUER_GOOGLE = "Google"
    const val CERT_ISSUER_ANDROID = "Android"

    // Date/Time Patterns
    const val DATE_FORMAT_EXPORT = "yyyy-MM-dd_HH-mm-ss"

    // Symbols
    const val SYMBOL_CHECK = "✓"
    const val SYMBOL_CROSS = "✕"
    const val SYMBOL_INFO = "ℹ"
    const val SYMBOL_WARNING = "⚠"

    // Export Labels (English)
    const val EXPORT_LABEL_REPORT_TITLE = "Android Application Security Report"
    const val EXPORT_LABEL_SCAN_DATE = "Scan Date"
    const val EXPORT_LABEL_SCAN_TIME = "Scan Time"
    const val EXPORT_LABEL_TOTAL_APPS = "Total Apps"
    const val EXPORT_LABEL_SUSPICIOUS_APPS = "Suspicious Apps"
    const val EXPORT_LABEL_APPLICATION = "Application"
    const val EXPORT_LABEL_PACKAGE = "Package"
    const val EXPORT_LABEL_VERSION = "Version"
    const val EXPORT_LABEL_SHA256 = "SHA256"
    const val EXPORT_LABEL_CERT_SHA256 = "Certificate SHA256"
    const val EXPORT_LABEL_INSTALLER = "Installer"
    const val EXPORT_LABEL_TARGET_SDK = "Target SDK"
    const val EXPORT_LABEL_MIN_SDK = "Min SDK"
    const val EXPORT_LABEL_RISK_SCORE = "Risk Score"
    const val EXPORT_LABEL_RISK_LEVEL = "Risk Level"
    const val EXPORT_LABEL_REASONS = "Reasons"
    const val EXPORT_LABEL_APK_NAME = "APK Name"
    const val EXPORT_LABEL_APK_PATH = "APK Path"
    const val EXPORT_LABEL_APK_SIZE = "APK Size"
    const val EXPORT_LABEL_MD5 = "MD5"
    const val EXPORT_LABEL_SHA1 = "SHA1"
    const val EXPORT_LABEL_SUBJECT = "Subject"
    const val EXPORT_LABEL_SERIAL = "Serial"
    const val EXPORT_LABEL_ALGORITHM = "Algorithm"
    const val EXPORT_LABEL_VALID_FROM = "Valid From"
    const val EXPORT_LABEL_VALID_TO = "Valid To"
    const val EXPORT_LABEL_ISSUER = "Issuer"
    const val EXPORT_LABEL_SYSTEM_APP = "System App"
    const val EXPORT_LABEL_DEBUGGABLE = "Debuggable"
    const val EXPORT_LABEL_ENABLED = "Enabled"
    const val EXPORT_LABEL_SUSPICIOUS = "Suspicious"
    const val EXPORT_LABEL_VIRUSTOTAL = "VirusTotal"
    const val EXPORT_LABEL_CHECKED = "Checked"
    const val EXPORT_LABEL_RESULT = "Result"
    const val EXPORT_LABEL_SCAN = "Scan"
    const val EXPORT_LABEL_DATE = "Date"
    const val EXPORT_LABEL_TIME = "Time"

    // Risk Reasons (English - for DB and Export)
    const val RISK_REASON_DEBUGGABLE = "Debuggable Application"
    const val RISK_REASON_UNKNOWN_INSTALLER = "Unknown Installer"
    const val RISK_REASON_OVERLAY = "Overlay Permission"
    const val RISK_REASON_INSTALL_PACKAGES = "Can Install APK"
    const val RISK_REASON_ACCESSIBILITY = "Accessibility Service"
    const val RISK_REASON_MICROPHONE = "Microphone Access"
    const val RISK_REASON_CAMERA = "Camera Access"
    const val RISK_REASON_READ_SMS = "Read SMS"
    const val RISK_REASON_SEND_SMS = "Send SMS"
    const val RISK_REASON_AUTO_START = "Starts Automatically"

    // Recommendations (English - for DB and Export)
    const val RISK_REC_CRITICAL = "This application has a very high risk score. It is strongly recommended to upload the APK for advanced malware analysis."
    const val RISK_REC_HIGH = "This application appears suspicious. Uploading the APK for further security analysis is recommended."
    const val RISK_REC_MEDIUM = "Some suspicious indicators were detected. Monitor this application carefully."
    const val RISK_REC_SAFE = "No significant security risk detected."

    const val BALE_BOT_URL = "https://ble.ir/App_scanner_bot"

}