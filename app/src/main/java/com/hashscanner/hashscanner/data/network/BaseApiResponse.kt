package com.hashscanner.hashscanner.data.network

import android.util.Log
import com.hashscanner.hashscanner.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val body = response.body()
                    return@withContext if (body != null) {
                        NetworkResult.Success(message = "Success", data = body)
                    } else {
                        // Handle Unit or empty success responses
                        @Suppress("UNCHECKED_CAST")
                        NetworkResult.Success(message = "Success", data = Unit as T)
                    }
                }
                Log.e(Constants.TAG, "API Response Error: code=${response.code()}, message=${response.message()}")
                return@withContext error("code : ${response.code()} , message : ${response.message()}")
            } catch (e: Exception) {
                Log.e(Constants.TAG, "API Exception: ${e.message}", e)
                return@withContext error(e.message ?: e.toString())
            }
        }


    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error("Api call failed : $errorMessage")
}
