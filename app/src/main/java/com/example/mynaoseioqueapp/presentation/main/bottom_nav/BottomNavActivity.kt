package com.example.mynaoseioqueapp.presentation.main.bottom_nav

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.databinding.ActivityBottomNavBinding
import com.example.mynaoseioqueapp.presentation.qr_scanner.ObjectDetectionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.qrFloatingButton.setOnClickListener {
            startActivity(Intent(this@BottomNavActivity, ObjectDetectionActivity::class.java))
        }
    }
}