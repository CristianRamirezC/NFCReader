package com.example.nfcreader.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.nfcreader.databinding.FragmentLoginBinding
import com.example.nfcreader.ui.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewListeners(binding)
        initializeObservers(binding)
    }

    private fun initializeViewListeners(binding: FragmentLoginBinding) {
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

    private fun initializeObservers(binding: FragmentLoginBinding) {

        binding.loginButton.isClickable = true
        binding.loginButton.isEnabled = true

//        loginViewModel.isLoginButtonEnabled.observe(viewLifecycleOwner) {
//            Log.i("initializeObservers", "$it")
//            binding.loginButton.isClickable = it
//            binding.loginButton.isEnabled = it
//        }

        loginViewModel.isUserAbleToLogin.observe(viewLifecycleOwner) {
            if (it) {
                val action = LoginFragmentDirections.actionLoginFragmentToFunctionsFragment()
                findNavController().navigate(action)
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