package com.example.mynaoseioqueapp.presentation.check_table

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.repository.TableRepository
import com.example.mynaoseioqueapp.presentation.main.bottom_nav.cart.CartFragmentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckTableViewModel @Inject constructor(
    private val repository: TableRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    sealed class TableEvent {
        class Success(val id: Long) : TableEvent()
        class Failure(val errorText: String) : TableEvent()
        object Loading : TableEvent()
        object Empty : TableEvent()
    }

    private val _request = MutableStateFlow<TableEvent>(TableEvent.Empty)
    val request: StateFlow<TableEvent> = _request

    fun occupyTableRequest(authToken: String, id: Long) {
        viewModelScope.launch(dispatcher.io) {
            _request.value = TableEvent.Loading
            when (val apiResponse = repository.occupyTable(authToken, id)) {
                is Resource.Success -> {
                    _request.value = TableEvent.Success(apiResponse.data!!)
                }
                is Resource.Error -> {
                    _request.value = TableEvent.Failure(apiResponse.message!!)
                    Log.d("MyApiMessageeeeeeeee", apiResponse.message)
                }
            }
        }
    }

    private var _leaveTableRequest = MutableStateFlow<TableEvent>(TableEvent.Empty)
    val leaveTableRequest: StateFlow<TableEvent> = _leaveTableRequest

    fun leaveTableApi(authToken: String, tableId: String, sharedPreferences: SharedPreferences) {
        viewModelScope.launch(dispatcher.io) {
            _leaveTableRequest.value = TableEvent.Loading
            val id = tableId.toLong()
            when (val response = repository.leaveTable(authToken, id)) {
                is Resource.Success -> {
                    sharedPreferences.edit().remove(Constant.TABLE_ID).apply()
                    _leaveTableRequest.value = TableEvent.Success(id)
                }
                is Resource.Error -> {
                    _leaveTableRequest.value = TableEvent.Failure(response.message!!)
                }
            }
        }
    }
}