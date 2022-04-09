package com.example.mynaoseioqueapp.presentation.check_table

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.databinding.ActivityCheckTableBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CheckTableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckTableBinding

    private val checkTableViewModel: CheckTableViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = this.getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

//        check(intent.hasExtra("selectedTable")) { "No table selected" }
//        check(sharedPref.contains(Constant.ACCESS_TOKEN)) { "No auth token" }

        val authToken = readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)!!

        val id = intent.getStringExtra("selectedTable")!!.toLong()

        Log.d("MyUrlllllllllllll", id.toString())

        checkTableViewModel.occupyTableRequest(authToken, id)

        lifecycleScope.launchWhenStarted {
            checkTableViewModel.request.collect { event ->
                when (event) {
                    is CheckTableViewModel.TableEvent.Success -> {
                        binding.tableProgressBar.visibility = View.GONE
                        saveData(Constant.TABLE_ID, event.id.toString(), sharedPref)
                        finish()
                    }
                    CheckTableViewModel.TableEvent.Loading -> {
                        binding.tableProgressBar.visibility = View.VISIBLE
                    }
                    is CheckTableViewModel.TableEvent.Failure -> {
                        binding.tableProgressBar.visibility = View.GONE
                        Toast.makeText(
                            this@CheckTableActivity,
                            "Could not occupy table",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun readDataFromSharedPref(key: String, sharedPref: SharedPreferences): String? {
        return sharedPref.getString(key, "Token Not Found")
    }

    private fun saveData(key: String, value: String, sharedPref: SharedPreferences) {
        sharedPref.edit().putString(key, value).apply()
    }
}