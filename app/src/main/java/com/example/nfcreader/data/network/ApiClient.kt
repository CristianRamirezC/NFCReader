package com.example.nfcreader.data.network

import com.example.nfcreader.data.model.LoginRequest
import com.example.nfcreader.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

private const val LOGIN_URL = "sanctum/token"

interface ApiClient {
    @POST(LOGIN_URL)
    suspend fun login(@Body configurationRequest: LoginRequest): Response<LoginResponse>
}