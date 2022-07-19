package com.example.mynaoseioqueapp.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.common.Resource
import com.example.mynaoseioqueapp.data.remote.dto.AuthenticationResponse
import com.example.mynaoseioqueapp.data.remote.dto.CreateUserRequest
import com.example.mynaoseioqueapp.domain.repository.UserRepository
import com.example.mynaoseioqueapp.util.UserValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import io.konform.validation.ValidationErrors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
//
    sealed class CreateUserEvent {
        class Success(val token: AuthenticationResponse): SignUpViewModel.CreateUserEvent()
        class Failure(val erro: String): SignUpViewModel.CreateUserEvent()
        object Loading: SignUpViewModel.CreateUserEvent()
        object Empty: SignUpViewModel.CreateUserEvent()
    }
//
    private val _request = MutableStateFlow<CreateUserEvent>(CreateUserEvent.Empty)
    val request: StateFlow<CreateUserEvent> = _request
//
    fun createUserPostRequest(userRequest: CreateUserRequest) {
        viewModelScope.launch(dispatcher.io) {
            _request.value = CreateUserEvent.Loading
            when(val createUserResponse = repository.createUserPostRequest(userRequest)){
                is Resource.Success -> {
                    _request.value = CreateUserEvent.Success(createUserResponse.data!!)
                }
                is Resource.Error -> {
                    _request.value = CreateUserEvent.Failure(createUserResponse.message!!)
                }
            }
        }
    }

    fun verifyUser(createUserRequest: CreateUserRequest): ValidationErrors? {
        val validation = UserValidation.validateUserRequest(createUserRequest)
        if (validation.isNullOrEmpty()) {
            return null
        } else {
            return validation
        }
    }
}