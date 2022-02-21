package com.example.mynaoseioqueapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginResponse
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    sealed class UserEvent {
        class Success(val userResponse: UserLoginResponse) : LoginViewModel.UserEvent()
        class Failure(val errorText: String) : LoginViewModel.UserEvent()
        object Loading : LoginViewModel.UserEvent()
        object Empty : LoginViewModel.UserEvent()
    }

    private val _request = MutableStateFlow<UserEvent>(UserEvent.Empty)
    val request : StateFlow<UserEvent> = _request

    fun loginUserRequestFromApi(username: String, password: String) {
        viewModelScope.launch(dispatcher.io){
            _request.value = UserEvent.Loading
            when(val userResponse = repository.loginUserPostRequest(username, password)){
                is Resource.Error -> {
                    _request.value = UserEvent.Failure(userResponse.message!!)
                }
                is Resource.Success -> {
                    _request.value = UserEvent.Success(userResponse.data!!)
                }
            }
        }
    }
}