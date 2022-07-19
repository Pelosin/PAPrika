package com.example.mynaoseioqueapp.util

import com.example.mynaoseioqueapp.data.remote.dto.CreateUserRequest
import io.konform.validation.Validation
import io.konform.validation.ValidationErrors
import io.konform.validation.jsonschema.minLength

object UserValidation {
    val pattern = "'^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$'".toRegex()
    private val userValidationRules = Validation<CreateUserRequest> {
        CreateUserRequest::username {
            minLength(4) hint "Username must be longer than 4 chars"
        }
        CreateUserRequest::password {
            minLength(6) hint "Password is too weak"
        }
    }

    fun validateUserRequest(createUserRequest: CreateUserRequest): ValidationErrors? {
        val validation = userValidationRules.validate(createUserRequest)
        return if (validation.errors.isNullOrEmpty()) {
            null
        } else {
            validation.errors
        }
    }
}