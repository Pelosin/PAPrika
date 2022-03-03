package com.example.mynaoseioqueapp.presentation.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.databinding.ActivityLoginBinding
import com.example.mynaoseioqueapp.presentation.main.bottom_nav.BottomNavActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPref = this.getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

        binding.loginButton.setOnClickListener {
            viewModel.loginUserRequestFromApi(
                "fff",
                "1234"
            )

        }

        lifecycleScope.launchWhenStarted {
            viewModel.request.collect { event ->
                when(event){
                    is LoginViewModel.UserEvent.Failure -> {
                        Log.d("MyLogggggggg", event.errorText)
                    }
                    is LoginViewModel.UserEvent.Loading -> {

                    }
                    is LoginViewModel.UserEvent.Success -> {
//                        Log.d("MyLoggggggggg", event.userResponse.access_token)
                        saveData(Constant.ACCESS_TOKEN, event.userResponse.access_token, sharedPref)
                        saveData(Constant.REFRESH_TOKEN, event.userResponse.refresh_token, sharedPref)
                        startActivity(Intent(
                            this@LoginActivity, BottomNavActivity::class.java
                        ))
                    }
                }
            }
        }
    }

    private fun saveData(key: String, value: String, sharedPref: SharedPreferences){
        sharedPref.edit().putString(key, value).apply()
    }

//    private suspend fun readData(key: String): String?{
//
//    }

}