package com.example.nfcreader.ui.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcreader.data.model.LoginRequest
import com.example.nfcreader.data.model.LoginResponse
import com.example.nfcreader.domain.LoginUseCase
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private var _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private var _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password

    private var _isUserAbleToLogin = MutableLiveData<Boolean>(false)
    val isUserAbleToLogin: LiveData<Boolean> = _isUserAbleToLogin

    private var _loginResponseMessage = MutableLiveData<String>("")
    val loginResponseMessage: LiveData<String> = _loginResponseMessage

    private var _isLoginButtonEnabled = MutableLiveData<Boolean>(false)
    val isLoginButtonEnabled: LiveData<Boolean> = _isLoginButtonEnabled

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginUserObject = LoginRequest(
                    email = email,
                    password = password,
                    deviceName = "postman"
                )
                val loginResponse: LoginResponse = loginUseCase.loginUser(loginUserObject)
                Log.i("LoginResponse", "$loginResponse")
                if (loginResponse.message == "ok") {
                    _isUserAbleToLogin.postValue(true)
                    _loginResponseMessage.postValue(loginResponse.message)
                } else {
                    _isUserAbleToLogin.postValue(false)
                    _loginResponseMessage.postValue(loginResponse.message)
                }

            } catch (e: Exception) {
                Log.e("UserLoginError", "$e")
            }
        }
    }

    /**
     * updates the email and password liveData when the login fields are being
     * modified
     **/
    fun onLoginInformationChanged(email: String, password: String): Unit {
        _email.postValue(email)
        _password.postValue(password)

        Log.i("onLoginInformationChanged", "$email, $password")
        _isLoginButtonEnabled.postValue(enableLoginButton(email, password))
    }

    /**
     * Validates if the login information is valid
     * in order to enable the login button and avoid the user to try to login with
     * blank field
     **/
    private fun enableLoginButton(email: String, password: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()
}