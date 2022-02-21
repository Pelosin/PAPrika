package com.example.mynaoseioqueapp.presentation.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.databinding.ActivityLoginBinding
import com.example.mynaoseioqueapp.presentation.home.DisplayInformationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = this.getSharedPreferences(
            "getString(R.string.app_preferences)", Context.MODE_PRIVATE
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
                        Log.d("MyLoggggggggg", event.userResponse.access_token)
//                        val accessToken = event.userResponse.access_token
//                        val refreshToken = event.userResponse.refresh_token
//                        sharedPref.edit().putString(PreferenceKeys.ACCESS_TOKEN, event.userResponse.access_token).apply()
                        saveData(PreferenceKeys.ACCESS_TOKEN, event.userResponse.access_token, sharedPref)
                        saveData(PreferenceKeys.REFRESH_TOKEN, event.userResponse.refresh_token, sharedPref)
                        startActivity(Intent(
                            this@LoginActivity, DisplayInformationActivity::class.java
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

    object PreferenceKeys {
        val ACCESS_TOKEN = "access_token"
        val REFRESH_TOKEN = "refresh_token"
    }
}