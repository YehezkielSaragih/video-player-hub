package com.example.videoplayerhub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoplayerhub.config.Client
import com.example.videoplayerhub.dto.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState = MutableLiveData<Result<String>>()
    val loginState: LiveData<Result<String>> get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = Client.authApi.login(LoginRequest(email, password))
                if (response.token.isNotEmpty()) {
                    _loginState.value = Result.success(response.token)
                } else {
                    _loginState.value = Result.failure(Exception("Empty token"))
                }
            } catch (e: Exception) {
                _loginState.value = Result.failure(e)
            }
        }
    }
}