package com.example.hashscanner.data.network

import com.example.hashscanner.data.model.api.ApkUploadResponse
import com.example.hashscanner.data.model.api.AppReport
import com.example.hashscanner.data.model.api.AuthenticationResponse
import com.example.hashscanner.data.model.api.ScanResultModel
import com.example.hashscanner.data.model.api.ScanResultResponse
import com.example.hashscanner.data.model.api.UserAuthentication
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
        @Part apk: MultipartBody.Part
    ): Response<ApkUploadResponse>



    @POST("api/scan_result")
    suspend fun getScanResult(
        @Body scanResultModel: ScanResultModel
    ): Response<ScanResultResponse>

}
