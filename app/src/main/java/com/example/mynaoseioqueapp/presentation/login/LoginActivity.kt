package com.example.mynaoseioqueapp.presentation.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginRequest
import com.example.mynaoseioqueapp.databinding.ActivityLoginBinding
import com.example.mynaoseioqueapp.presentation.main.bottom_nav.BottomNavActivity
import com.example.mynaoseioqueapp.presentation.sign_up.SignUpActivity
//import com.example.mynaoseioqueapp.presentation.sign_up.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginProgressBar.visibility = View.GONE


        val sharedPref = this.getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

        binding.loginButton.setOnClickListener {
            if (binding.usernameEditText.text.isNullOrEmpty()
                || binding.passwordEditText.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Por favor preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                    viewModel.loginUserRequestFromApi(
                        UserLoginRequest(
                            binding.usernameEditText.text.toString(),
                            binding.passwordEditText.text.toString()
                        )
                    )
                }
            }

        binding.singUpTextButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.request.collect { event ->
                when (event) {
                    is LoginViewModel.UserEvent.Failure -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciais invalidas",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("MyLogggggggg", event.errorText)
                        binding.usernameEditText.visibility = View.VISIBLE
                        binding.passwordEditText.visibility = View.VISIBLE
                        binding.loginProgressBar.visibility = View.GONE
                    }
                    is LoginViewModel.UserEvent.Loading -> {
                        binding.usernameEditText.visibility = View.GONE
                        binding.passwordEditText.visibility = View.GONE
                        binding.loginProgressBar.visibility = View.VISIBLE
                    }
                    is LoginViewModel.UserEvent.Success -> {
//                        Log.d("MyLoggggggggg", event.userResponse.access_token)
                        saveData(Constant.ACCESS_TOKEN, "Bearer ${event.authenticationResponse.jwtToken}", sharedPref)
                        startActivity(
                            Intent(
                                this@LoginActivity, BottomNavActivity::class.java
                            )
                        )
                        finish()
                    }
                }
            }
        }
    }

    private fun saveData(key: String, value: String, sharedPref: SharedPreferences) {
        sharedPref.edit().putString(key, value).apply()
    }

//    private suspend fun readData(key: String): String?{
//
//    }

}