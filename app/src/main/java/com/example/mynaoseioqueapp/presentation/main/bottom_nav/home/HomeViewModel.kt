package com.example.mynaoseioqueapp.presentation.main.bottom_nav.home

import androidx.lifecycle.*
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.model.Category
import com.example.mynaoseioqueapp.domain.repository.CategoryRepository
import com.example.mynaoseioqueapp.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val repository: FoodRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel(){

    sealed class CategoryEvent {
        class Success(val categoryList: List<Category>) : CategoryEvent()
        class Failure(val errorText: String) : CategoryEvent()
        object Loading : CategoryEvent()
        object Empty : CategoryEvent()
    }

    private val _request = MutableStateFlow<CategoryEvent>(CategoryEvent.Empty)
    val request: StateFlow<CategoryEvent> = _request

    fun requestCategoryFromApi(jwtToken: String) {
        viewModelScope.launch(dispatcher.io) {
            _request.value = CategoryEvent.Loading
            when(val categoryResponse = categoryRepository.getCategories(jwtToken)) {
                is Resource.Error -> {
                    _request.value = CategoryEvent.Failure(categoryResponse.message!!)
                }
                is Resource.Success -> {
                    val foods = categoryResponse.data!!
                    _request.value = CategoryEvent.Success(foods)
                }
            }
        }
    }

//    sealed class FoodEvent{
//        class Success(val foodList: List<Food>) : FoodEvent()
//        class Failure(val errorText: String) : FoodEvent()
//        object Loading : FoodEvent()
//        object Empty : FoodEvent()
//    }
//
//    private val _request = MutableStateFlow<FoodEvent>(FoodEvent.Empty)
//    val request: StateFlow<FoodEvent> = _request
//
//    fun requestFoodFromApi(authToken: String) {
//        viewModelScope.launch(dispatcher.io) {
//            _request.value = FoodEvent.Loading
//            when(val foodResponse = repository.getFoods(authToken)) {
//                is Resource.Error -> {
//                    _request.value = FoodEvent.Failure(foodResponse.message!!)
//                }
//                is Resource.Success -> {
//                    val foods = foodResponse.data!!
//                    _request.value = FoodEvent.Success(foods)
//                }
//            }
//        }
//    }
}