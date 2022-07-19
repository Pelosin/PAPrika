package com.example.mynaoseioqueapp.presentation.food_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.databinding.ActivityFoodDetailsBinding
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.util.LineOrderSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.math.BigDecimal

@AndroidEntryPoint
class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDetailsBinding

    private val detailsViewModel: FoodDetailsViewModel by viewModels()

    private var quantity: Int = 1

    private var displayPrice = BigDecimal(0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        check(intent.hasExtra(Constant.FOOD_SERIALIZABLE)) { "Something went wrong in the app" }

        val foodToBeDisplayed = intent.getSerializableExtra(Constant.FOOD_SERIALIZABLE) as Food

        binding.foodDetailsName.text = foodToBeDisplayed.name
        binding.foodDescriptionDetailsTextView.text = foodToBeDisplayed.description

        binding.priceDetailsTextView.text = foodToBeDisplayed.price.toEngineeringString()

        foodToBeDisplayed.url.let {
            binding.foodDetailsImageView.load(foodToBeDisplayed.url)
        }

        binding.addButton.setOnClickListener {
            if (quantity == 1) return@setOnClickListener
            quantity -= 1
            displayPrice = foodToBeDisplayed.price * quantity.toBigDecimal()
            binding.quantityDetailsTextView.text = quantity.toString()
            binding.priceDetailsTextView.text = displayPrice.toString()
        }

        binding.subButton.setOnClickListener {
            quantity += 1
            displayPrice = foodToBeDisplayed.price * quantity.toBigDecimal()
            binding.quantityDetailsTextView.text = quantity.toString()
            binding.priceDetailsTextView.text = displayPrice.toString()
        }

        binding.addToCartButton.setOnClickListener {
            detailsViewModel.setLineOrderList(foodToBeDisplayed, quantity)
        }

        lifecycleScope.launchWhenStarted {
            detailsViewModel.request.collect { event ->
                when(event) {
                    is FoodDetailsViewModel.SetLineOrderEvent.Success -> {
                        Toast.makeText(this@FoodDetailsActivity,
                            "Your item was added to the cart",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is FoodDetailsViewModel.SetLineOrderEvent.Loading -> {

                    }
                    is FoodDetailsViewModel.SetLineOrderEvent.Failure -> {
                        Toast.makeText(this@FoodDetailsActivity,
                            "Something went wrong, try again",
                            Toast.LENGTH_SHORT).show()
                        Log.d("MyLogggggErrorrrrrrrr", event.errorText)
                    }
                }
            }
        }
    }
}