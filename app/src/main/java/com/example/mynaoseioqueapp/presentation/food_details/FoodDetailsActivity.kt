package com.example.mynaoseioqueapp.presentation.food_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.databinding.ActivityFoodDetailsBinding
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.util.LineOrderSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDetailsBinding

    private val detailsViewModel: FoodDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        check(intent.hasExtra(Constant.FOOD_SERIALIZABLE)) { "Something went wrong in the app" }

        val foodToBeDisplayed = intent.getSerializableExtra(Constant.FOOD_SERIALIZABLE) as Food

        binding.objText.text = foodToBeDisplayed.name
        binding.objText2.text = foodToBeDisplayed.price.toEngineeringString()

        binding.orderButton.setOnClickListener {
            detailsViewModel.setLineOrderList(foodToBeDisplayed)
        }

        lifecycleScope.launchWhenStarted {
            detailsViewModel.request.collect { event ->
                when(event) {
                    is FoodDetailsViewModel.SetLineOrderEvent.Success -> {
                        Toast.makeText(this@FoodDetailsActivity,
                            "Food added in the cart",
                            Toast.LENGTH_SHORT).show()
                    }
                    is FoodDetailsViewModel.SetLineOrderEvent.Loading -> {

                    }
                    is FoodDetailsViewModel.SetLineOrderEvent.Failure -> {
                        Toast.makeText(this@FoodDetailsActivity,
                            "Food can not be added in the cart",
                            Toast.LENGTH_SHORT).show()
                        Log.d("MyLogggggErrorrrrrrrr", event.errorText)
                    }
                }
            }
        }
    }
}