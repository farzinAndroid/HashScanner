package com.example.hashscanner.data.network

import com.example.hashscanner.data.model.api.AppReport
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    

    @POST("api/report")
    suspend fun sendScanReport(@Body appReport: AppReport): Response<Unit>
    
}
