package com.example.mynaoseioqueapp.presentation.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.databinding.ActivityDisplayInformationBinding
import com.example.mynaoseioqueapp.presentation.adapters.HomeRecyclerAdapter
import com.example.mynaoseioqueapp.presentation.qr_scanner.ObjectDetectionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class DisplayInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayInformationBinding

    private val viewModel: DisplayInformationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.homeRecyclerView.setHasFixedSize(true)
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)

        val sharedPref = this.getSharedPreferences(
            "getString(R.string.app_preferences)", Context.MODE_PRIVATE
        )

        if(readDataFromSharedPref(PreferenceKeys.ACCESS_TOKEN, sharedPref) == "Token Not Found"){
            Log.d("MyLoggggggErrorrrrrrr", "Fudeo")
        }else{
            viewModel.requestFoodFromApi(readDataFromSharedPref(PreferenceKeys.ACCESS_TOKEN, sharedPref)!!)
        }
        binding.qrFloatingButton.setOnClickListener {
            startActivity(Intent(this@DisplayInformationActivity, ObjectDetectionActivity::class.java))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.request.collect { event ->
                when(event) {
                    is DisplayInformationViewModel.FoodEvent.Failure -> {
                        Log.d("MyLoggggggFood", event.errorText)
                        binding.homeProgressBar.visibility = View.GONE
                    }
                    is DisplayInformationViewModel.FoodEvent.Loading -> {
                        binding.homeProgressBar.visibility = View.VISIBLE
                    }
                    is DisplayInformationViewModel.FoodEvent.Success -> {
                        Log.d("MyLoggggggFood", event.foodList.toString())
                        binding.homeRecyclerView.adapter = HomeRecyclerAdapter(applicationContext, event.foodList)
                        binding.homeProgressBar.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun readDataFromSharedPref(key: String, sharedPref: SharedPreferences): String?{
        return sharedPref.getString(key, "Token Not Found")
    }

    object PreferenceKeys {
        val ACCESS_TOKEN = "access_token"
        val REFRESH_TOKEN = "refresh_token"
    }
}