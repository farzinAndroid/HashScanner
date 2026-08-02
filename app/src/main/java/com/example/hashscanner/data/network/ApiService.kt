package com.example.hashscanner.data.network

import com.example.hashscanner.data.model.api.AppReport
import com.example.hashscanner.data.model.api.AuthenticationResponse
import com.example.hashscanner.data.model.api.UserAuthentication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    

    @POST("api/report")
    suspend fun sendScanReport(@Body appReport: AppReport): Response<Unit>



    @POST("api/activate")
    suspend fun authenticate(@Body userAuthentication: UserAuthentication): Response<AuthenticationResponse>
    
}
