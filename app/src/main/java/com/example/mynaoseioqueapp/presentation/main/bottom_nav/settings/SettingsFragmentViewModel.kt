package com.example.mynaoseioqueapp.presentation.main.bottom_nav.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.dto.OrderByIdResponse
import com.example.mynaoseioqueapp.data.remote.dto.OrderHistoryResponse
import com.example.mynaoseioqueapp.data.remote.dto.UserResponse
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.domain.repository.OrderRepository
import com.example.mynaoseioqueapp.domain.repository.UserRepository
import com.example.mynaoseioqueapp.presentation.main.bottom_nav.cart.CartFragmentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val userRepository: UserRepository,
    private val dispatcher: DispatcherProvider
)  : ViewModel() {

    sealed class OrderHistoryEvent {
        class Success(val orderHistoryResponse: List<OrderHistoryResponse>) : OrderHistoryEvent()
        class Error(val error: String) : OrderHistoryEvent()
        object Loading : OrderHistoryEvent()
        object Empty : OrderHistoryEvent()
    }

    private val _orderHistoryRequest = MutableStateFlow<OrderHistoryEvent>(OrderHistoryEvent.Empty)
    val orderHistoryRequest: StateFlow<OrderHistoryEvent> =  _orderHistoryRequest

    fun getOrderHistory(authToken: String) {
        viewModelScope.launch (dispatcher.io) {
            _orderHistoryRequest.value = OrderHistoryEvent.Loading
            try {
                when(val orderHistoryResponse = repository.getOrderFromUser(authToken)) {
                    is Resource.Success -> {
                        _orderHistoryRequest.value = OrderHistoryEvent.Success(orderHistoryResponse.data!!)
                    }
                    is Resource.Error -> {
                        Log.d("MyOrderHistoryError", orderHistoryResponse.message!!)
                        _orderHistoryRequest.value = OrderHistoryEvent.Error(orderHistoryResponse.message!!)
                    }
                }
            } catch (e: Exception) {
                Log.d("MyOrderHistoryError", e.localizedMessage)
            }
        }
    }

    sealed class GetUserEvent {
        class Success(val userResponse: UserResponse) : GetUserEvent()
        class Error(val error: String) : GetUserEvent()
        object Loading : GetUserEvent()
        object Empty : GetUserEvent()
    }

    private val _getUserRequest = MutableStateFlow<GetUserEvent>(GetUserEvent.Empty)
    val getUserRequest: StateFlow<GetUserEvent> =  _getUserRequest

    fun getUserByToken(authToken: String) {
        viewModelScope.launch (dispatcher.io) {
            _getUserRequest.value = GetUserEvent.Loading
            try {
                when(val userResponse = userRepository.getUserWithToken(authToken)) {
                    is Resource.Success -> {
                        _getUserRequest.value = GetUserEvent.Success(userResponse.data!!)
                    }
                    is Resource.Error -> {
                        _getUserRequest.value = GetUserEvent.Error(userResponse.message!!)
                    }
                }
            } catch (e: Exception) {
                Log.d("MyUserByTokenLog", e.localizedMessage)
            }
        }
    }

    sealed class GetOrderByIdEvent {
        class Success(val orderByIdResponse: OrderByIdResponse): GetOrderByIdEvent()
        class Error(val error: String) : GetOrderByIdEvent()
        object Loading : GetOrderByIdEvent()
        object Empty : GetOrderByIdEvent()
    }

    private val _getOrderByIdRequest = MutableStateFlow<GetOrderByIdEvent>(GetOrderByIdEvent.Empty)
    val getOrderByIdRequest: StateFlow<GetOrderByIdEvent> = _getOrderByIdRequest

    fun getOrderById(authToken: String, id: Long) {
        viewModelScope.launch(dispatcher.io) {
            _getOrderByIdRequest.value = GetOrderByIdEvent.Loading
            try {
                when(val getOrderByIdResponse = repository.getOrderById(authToken, id)){
                    is Resource.Success -> {
                        _getOrderByIdRequest.value = GetOrderByIdEvent.Success(getOrderByIdResponse.data!!)
                    }
                    is Resource.Error -> {
                        _getOrderByIdRequest.value = GetOrderByIdEvent.Error(getOrderByIdResponse.message!!)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}