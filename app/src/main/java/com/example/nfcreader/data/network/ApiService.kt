package com.example.nfcreader.data.network

import com.example.nfcreader.data.model.LoginResponse
import com.example.nfcreader.data.model.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ApiService @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun login(configurationRequest: LoginRequest): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            apiClient.login(configurationRequest)
        }
    }
}