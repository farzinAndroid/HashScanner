<div align="center">

# 🛡️ Hash Scanner

**An Android security scanner that checks installed apps for suspicious behavior and reports findings to a backend for deeper analysis.**

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![minSdk](https://img.shields.io/badge/minSdk-24-blue)
![Status](https://img.shields.io/badge/Status-In%20Development-orange)

</div>

---

## 📸 Screenshots

<div align="center">

<img src="docs/screenshots/screenshot_1.png" width="48%" alt="App screens 1" />
<img src="docs/screenshots/screenshot_2.png" width="48%" alt="App screens 2" />

</div>

---

## ✨ Features

- 🔍 **Device scan** – inspects every installed app and computes MD5, SHA-1 and SHA-256 hashes plus certificate details.
- 🧮 **Risk scoring** – scores each app from 0 to 100 (`SAFE` to `CRITICAL`) based on permissions, installer source and more.
- ☁️ **Cloud analysis** – suspicious apps are reported to the server and reviewed; results come back as `SAFE`, `SUSPICIOUS` or `VIRUS`.
- 📤 **Optional APK upload** – the user decides whether to send a suspicious app's APK for review.
- 🔔 **Background notifications** – WorkManager notifies you when a result is ready, even if the app is closed.
- 🗂️ **Scan history** – past scans and their results are stored locally.

---

## 🧰 Tech Stack

Kotlin · Jetpack Compose · Material 3 · Hilt · Room · DataStore · Retrofit/OkHttp · WorkManager · Navigation Compose

Layered **MVVM**: `data` (scanners, analyzer, network, database) → `repository` → `viewmodel` → `ui`.

---

## 🚀 Getting Started

1. Clone the repo
   ```bash
   git clone https://github.com/farzinAndroid/HashScanner.git
   ```
2. Add your backend URL to `local.properties`:
   ```properties
   BASE_URL="https://your-backend.example.com/"
   ```
3. Open in Android Studio (JDK 21), sync Gradle, and run.

**Requirements:** Android 7.0+ (API 24), Android SDK 36.

---

## 📄 License

No license specified yet.
