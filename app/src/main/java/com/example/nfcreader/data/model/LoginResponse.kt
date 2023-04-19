package com.example.nfcreader.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: LoginData = LoginData(),
    var message: String = ""
)

data class LoginData(
    @SerializedName("token")
    val token: String = ""
)