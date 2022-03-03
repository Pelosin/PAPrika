package com.example.mynaoseioqueapp.presentation.check_table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.domain.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckTableViewModel @Inject constructor(
    private val repository: TableRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel(){

    sealed class TableEvent{
        class Success(val id: Long): TableEvent()
        class Failure(val errorText: String) : TableEvent()
        object Loading: TableEvent()
        object Empty: TableEvent()
    }

    private val _request = MutableStateFlow<TableEvent>(TableEvent.Empty)
    val request: StateFlow<TableEvent> =  _request

    fun occupyTableRequest(authToken: String, url: String){
        viewModelScope.launch (dispatcher.io){
            _request.value = TableEvent.Loading
            when(val apiResponse  = repository.occupyTable(authToken, url)){
                is Resource.Success -> {
                    _request.value = TableEvent.Success(apiResponse.data!!)
                }
                is Resource.Error -> {
                    _request.value = TableEvent.Failure(apiResponse.message!!)
                }
            }
        }
    }
}