package com.example.nfcreader.data.repository

import com.example.nfcreader.data.model.LoginResponse
import com.example.nfcreader.data.model.LoginRequest
import com.example.nfcreader.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {
    //login using API
    suspend fun loginApi(configurationRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(configurationRequest)
    }
}