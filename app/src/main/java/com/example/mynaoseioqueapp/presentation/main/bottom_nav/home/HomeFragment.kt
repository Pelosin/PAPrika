package com.example.mynaoseioqueapp.presentation.main.bottom_nav.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.databinding.FragmentHomeBinding
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.presentation.adapters.HomeRecyclerAdapter
import com.example.mynaoseioqueapp.presentation.food_details.FoodDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeRecyclerAdapter.HomeViewHolder.OnClickHomeEvent {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeRecyclerView.setHasFixedSize(true)
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false)

        val sharedPref = requireActivity().getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

        if(readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref) == "Token Not Found"){
            Log.d("MyLoggggggErrorrrrrrr", "Token not found")
        }else{
            viewModel.requestFoodFromApi(readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)!!)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.request.collect { event ->
                when(event) {
                    is HomeViewModel.FoodEvent.Success -> {
//                        Log.d("MyLoggggggFood", event.foodList.toString())
                        binding.homeRecyclerView.adapter = HomeRecyclerAdapter(requireContext(), event.foodList, this@HomeFragment)
                        binding.homeProgressBar.visibility = View.GONE
                    }
                    is HomeViewModel.FoodEvent.Loading -> {
                        binding.homeProgressBar.visibility = View.VISIBLE
                    }
                    is HomeViewModel.FoodEvent.Failure -> {
                        Log.d("MyLoggggggFood", event.errorText)
                        binding.homeProgressBar.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun readDataFromSharedPref(key: String, sharedPref: SharedPreferences): String?{
        return sharedPref.getString(key, "Token Not Found")
    }

    override fun onClickEvent(food: Food) {
        startActivity(Intent(
            context, FoodDetailsActivity::class.java
        ).putExtra(Constant.FOOD_SERIALIZABLE, food))
    }
}