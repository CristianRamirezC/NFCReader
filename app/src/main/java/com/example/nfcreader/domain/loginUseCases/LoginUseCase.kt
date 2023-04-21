package com.example.nfcreader.domain.loginUseCases

import com.example.nfcreader.data.model.LoginResponse
import com.example.nfcreader.data.model.LoginRequest
import com.example.nfcreader.data.repository.ApiRepository
import retrofit2.Response
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    //In the use case we can apply complex logic, we can validate if there is no connection
    //so we can login using a database or use the API instead so we don't need to
    //implement complex logic in the data layer
    suspend fun loginUser(configurationRequest: LoginRequest): LoginResponse {
        val loginResponse: Response<LoginResponse> = repository.loginApi(configurationRequest)
        return when(loginResponse.code()) {
            200 -> loginResponse.body().apply { this?.message = "ok"}!!
            422 -> loginResponse.body().apply { this?.message = "The provided credentials are incorrect."}!!
            else -> loginResponse.body().apply { this?.message = "An error has occurred when trying to login"}!!
        }
    }
}