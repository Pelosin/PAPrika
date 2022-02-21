package com.example.mynaoseioqueapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayInformationViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel(){

    sealed class FoodEvent{
        class Success(val foodList: List<Food>) : FoodEvent()
        class Failure(val errorText: String) : FoodEvent()
        object Loading : FoodEvent()
        object Empty : FoodEvent()
    }

    private val _request = MutableStateFlow<FoodEvent>(FoodEvent.Empty)
    val request: StateFlow<FoodEvent> = _request

    fun requestFoodFromApi(authToken: String) {
        viewModelScope.launch(dispatcher.io) {
            _request.value = FoodEvent.Loading
            when(val foodResponse = repository.getFoods(authToken)) {
                is Resource.Error -> {
                    _request.value = FoodEvent.Failure(foodResponse.message!!)
                }
                is Resource.Success -> {
                    val foods = foodResponse.data!!
                    _request.value = FoodEvent.Success(foods)
                }
            }
        }
    }
}