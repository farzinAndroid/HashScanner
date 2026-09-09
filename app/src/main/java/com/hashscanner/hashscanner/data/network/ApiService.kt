package com.hashscanner.hashscanner.data.network

import com.hashscanner.hashscanner.data.model.api.ApkUploadResponse
import com.hashscanner.hashscanner.data.model.api.AppReport
import com.hashscanner.hashscanner.data.model.api.AuthenticationResponse
import com.hashscanner.hashscanner.data.model.api.ScanResultModel
import com.hashscanner.hashscanner.data.model.api.ScanResultResponse
import com.hashscanner.hashscanner.data.model.api.UserAuthentication
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    

    @POST("api/report")
    suspend fun sendScanReport(@Body appReport: AppReport): Response<Unit>



    @POST("api/activate")
    suspend fun authenticate(@Body userAuthentication: UserAuthentication): Response<AuthenticationResponse>

    @Multipart
    @POST("api/upload_apk")
    suspend fun uploadApk(
        @Part("package") packageName: RequestBody,
        @Part("scanId") scanId: RequestBody,
        @Part("deviceId") deviceId: RequestBody,
        @Part("appName") appName: RequestBody,
        @Part("sha256") sha256: RequestBody,
        @Part apk: MultipartBody.Part
    ): Response<ApkUploadResponse>



    @POST("api/scan_finished")
    suspend fun scanFinished(
        @Body  scanResultModel: ScanResultModel
    ): Response<ScanFinishedResponse>


    @POST("api/scan_result")
    suspend fun getScanResult(
        @Body scanResultModel: ScanResultModel
    ): Response<ScanResultResponse>

}
