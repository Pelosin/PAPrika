package com.example.mynaoseioqueapp.presentation.sign_up

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.data.remote.dto.CreateUserRequest
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginRequest
import com.example.mynaoseioqueapp.databinding.ActivitySignUpBinding
import com.example.mynaoseioqueapp.presentation.main.bottom_nav.BottomNavActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = this.getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

        binding.signUpButton.setOnClickListener {
            if (binding.usernameEditText.text.isNullOrEmpty()
                || binding.passwordEditText.text.isNullOrEmpty()
                || binding.nameEditText.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Por favor preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val createUserRequest = CreateUserRequest(
                    binding.nameEditText.text.toString(),
                    binding.usernameEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
                val verifyUser = viewModel.verifyUser(createUserRequest)
                if(verifyUser.isNullOrEmpty()) {
                    viewModel.createUserPostRequest(createUserRequest)
                } else {
                    verifyUser.forEach { error ->
                        when {
                            error.dataPath.contains("username") -> {
                                binding.usernameEditText.error = error.message
                            }
                            error.dataPath.contains("password") -> {
                                binding.passwordEditText.error = error.message
                            }
                        }
                    }
                }
            }

        }

        binding.loginTextButton.setOnClickListener {
            onBackPressed()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.request.collect { event ->
                when (event) {
                    is SignUpViewModel.CreateUserEvent.Success -> {
                        saveData(Constant.ACCESS_TOKEN, "Bearer ${event.token.jwtToken}", sharedPref)
                        Log.d("MyCreatedUser", event.token.jwtToken)
                        startActivity(Intent(this@SignUpActivity, BottomNavActivity::class.java))
                    }
                    is SignUpViewModel.CreateUserEvent.Failure -> {
                        Log.d("MyErrorrrrrrr", event.erro)                    }
                    SignUpViewModel.CreateUserEvent.Loading -> {
                    }
                    SignUpViewModel.CreateUserEvent.Empty -> {
                    }
                }
            }
        }
    }

    private fun saveData(key: String, value: String, sharedPref: SharedPreferences) {
        sharedPref.edit().putString(key, value).apply()
    }
}