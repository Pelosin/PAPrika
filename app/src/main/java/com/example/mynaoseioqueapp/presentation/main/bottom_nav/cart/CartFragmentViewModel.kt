package com.example.mynaoseioqueapp.presentation.main.bottom_nav.cart

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.model.LineOrder
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.domain.repository.OrderRepository
import com.example.mynaoseioqueapp.domain.repository.TableRepository
import com.example.mynaoseioqueapp.presentation.food_details.FoodDetailsViewModel
import com.example.mynaoseioqueapp.util.LineOrderSetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val tableRepository: TableRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    sealed class OrderEvent {
        class Success(val orderResponse: Order) : OrderEvent()
        class Error(val errorText: String) : OrderEvent()
        object Loading : OrderEvent()
        object Empty : OrderEvent()
    }

    private val _orderRequest = MutableStateFlow<OrderEvent>(
        OrderEvent.Empty
    )
    val orderRequest: StateFlow<OrderEvent> = _orderRequest

    fun saveOrderOnApi(authToken: String, order: Order) {
        viewModelScope.launch(dispatcher.io) {
            _orderRequest.value = OrderEvent.Loading
            try {
                when(val orderResponse = repository.saveOrder(authToken, order)){
                    is Resource.Success -> {
                        _orderRequest.value = OrderEvent.Success(orderResponse.data!!)
                    }
                    is Resource.Error -> {
                        _orderRequest.value = OrderEvent.Error(orderResponse.message!!)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    sealed class LineOrderListEvent {
        class Success(var lineOrderList: List<LineOrder>, var totalPrice: BigDecimal) :
            LineOrderListEvent()

        class Error(val errorText: String) : LineOrderListEvent()
        object Loading : LineOrderListEvent()
        object Empty : LineOrderListEvent()
    }

    private val _lineOrderRequest = MutableStateFlow<LineOrderListEvent>(
        LineOrderListEvent.Empty
    )
    val lineOrderRequest: StateFlow<LineOrderListEvent> = _lineOrderRequest

    fun setLineOrderListWithPrices() {
//        val totalPrice: BigDecimal = BigDecimal(0)
        viewModelScope.launch(dispatcher.io) {
            _lineOrderRequest.value = LineOrderListEvent.Loading
            try {
                val lineOrderList = LineOrderSetter.getLineOrderList()
                var totalPrice: BigDecimal = BigDecimal(0)
                if (lineOrderList.isEmpty()) {
                    _lineOrderRequest.value = LineOrderListEvent.Success(lineOrderList, totalPrice)
                } else {
                    lineOrderList.forEach { lineOrder ->
                        lineOrder.partialPrice =
                            lineOrder.price.multiply(lineOrder.quantity.toBigDecimal())
                    }
                    lineOrderList.forEach { lineOrder ->
                        totalPrice += lineOrder.partialPrice
                    }
                    _lineOrderRequest.value = LineOrderListEvent.Success(lineOrderList, totalPrice)
                }
            } catch (e: Exception) {
                _lineOrderRequest.value = LineOrderListEvent.Error(e.localizedMessage)
            }
        }
    }

}