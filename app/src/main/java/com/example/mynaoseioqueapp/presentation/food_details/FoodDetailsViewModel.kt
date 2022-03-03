package com.example.mynaoseioqueapp.presentation.food_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.domain.model.LineOrder
import com.example.mynaoseioqueapp.presentation.check_table.CheckTableViewModel
import com.example.mynaoseioqueapp.util.LineOrderSetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {

    sealed class SetLineOrderEvent{
        class Success(lineOrder: LineOrder): SetLineOrderEvent()
        class Failure(val errorText: String) : SetLineOrderEvent()
        object Loading: SetLineOrderEvent()
        object Empty: SetLineOrderEvent()
    }

    private val _request = MutableStateFlow<SetLineOrderEvent>(SetLineOrderEvent.Empty)
    val request: StateFlow<SetLineOrderEvent> =  _request

    fun setLineOrderList(food:Food) {
        viewModelScope.launch (dispatcherProvider.default){
            _request.value = SetLineOrderEvent.Loading
            try {
                val lineOrder = LineOrder(food.name, 1)
                LineOrderSetter.setLineOrderList(lineOrder)
                _request.value = SetLineOrderEvent.Success(lineOrder)
            } catch (e: Exception){
                _request.value = SetLineOrderEvent.Failure(e.localizedMessage!!)
            }
        }
    }
}