<div align="center">

# 🛡️ Hash Scanner

**An Android security scanner that checks installed apps for suspicious behavior and reports findings to a backend for deeper analysis.**

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![minSdk](https://img.shields.io/badge/minSdk-24-blue)
![Status](https://img.shields.io/badge/Status-Done-brightgreen)

</div>

---

## 📸 Screenshots

<div align="center">

<img width="960" height="900" alt="photo_2_2026-10-16_11-06-01" src="https://github.com/user-attachments/assets/dfed8227-1d12-4941-8c78-284d837ab358" />

<img width="960" height="900" alt="photo_1_2026-10-16_11-06-01" src="https://github.com/user-attachments/assets/c174c215-5504-4f76-ab34-408c3e710245" />

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

**Requirements:** Android 7.0+ (API 24), Android SDK 36.

---
