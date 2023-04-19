package com.example.nfcreader.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nfcreader.ui.view.FunctionsActivity
import com.example.nfcreader.databinding.ActivityMainBinding
import com.example.nfcreader.ui.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initializeOnClickListeners(binding!!)
        initializeObservers(binding!!)
    }

    private fun initializeOnClickListeners(binding: ActivityMainBinding) {
        binding.emailField.afterTextChanged { email ->
            val password: String = loginViewModel.password.value!!
            loginViewModel.onLoginInformationChanged(
                email = email,
                password = password
            )
        }

        binding.passwordField.afterTextChanged { password ->
            val email: String = loginViewModel.email.value!!
            loginViewModel.onLoginInformationChanged(
                email = email,
                password = password
            )
        }

        binding.loginButton.setOnClickListener {
            val email: String = loginViewModel.email.value!!
            val password: String = loginViewModel.password.value!!
            loginViewModel.loginUser(email = email, password = password)
        }
    }

    private fun initializeObservers(binding: ActivityMainBinding) {

        loginViewModel.isLoginButtonEnabled.observe(this) {
            Log.i("initializeObservers", "$it")
            binding.loginButton.isClickable = it
            binding.loginButton.isEnabled = it
        }

        loginViewModel.isUserAbleToLogin.observe(this) {
            if (it) {
                startActivity(Intent(this, FunctionsActivity::class.java))
            }
        }
    }
}

/**
 * Extension function for the EditText views to add text changed listener
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}