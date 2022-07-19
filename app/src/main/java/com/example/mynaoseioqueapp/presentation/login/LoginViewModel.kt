package com.example.mynaoseioqueapp.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.dto.AuthenticationResponse
import com.example.mynaoseioqueapp.data.remote.dto.UserLoginRequest
import com.example.mynaoseioqueapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            _isLoading.value = false
        }
    }

    sealed class UserEvent {
        class Success(val authenticationResponse: AuthenticationResponse) : LoginViewModel.UserEvent()
        class Failure(val errorText: String) : LoginViewModel.UserEvent()
        object Loading : LoginViewModel.UserEvent()
        object Empty : LoginViewModel.UserEvent()
    }

    private val _request = MutableStateFlow<UserEvent>(UserEvent.Empty)
    val request: StateFlow<UserEvent> = _request

    fun loginUserRequestFromApi(userLoginRequest: UserLoginRequest) {
        viewModelScope.launch(dispatcher.io) {
            _request.value = UserEvent.Loading
            when (val userResponse = repository.loginUserPostRequest(userLoginRequest)) {
                is Resource.Error -> {
                    _request.value = UserEvent.Failure(userResponse.message!!)
                }
                is Resource.Success -> {
                    Log.d("MyUserResponse", userResponse.toString())
                    _request.value = UserEvent.Success(userResponse.data!!)
                }
            }
        }
    }
}