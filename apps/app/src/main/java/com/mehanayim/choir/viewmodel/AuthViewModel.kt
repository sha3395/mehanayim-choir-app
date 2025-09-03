package com.mehanayim.choir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehanayim.choir.data.model.User
import com.mehanayim.choir.data.repository.AuthRepository
import com.mehanayim.choir.MainActivity.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        viewModelScope.launch {
            authRepository.getCurrentUserFlow().collect { user ->
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _currentUser.value = null
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.signIn(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user)
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Sign in failed"
                }
            
            _isLoading.value = false
        }
    }
    
    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.signUp(email, password, name)
                .onSuccess { user ->
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user)
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Sign up failed"
                }
            
            _isLoading.value = false
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.resetPassword(email)
                .onSuccess {
                    _errorMessage.value = "Password reset email sent"
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Password reset failed"
                }
            
            _isLoading.value = false
        }
    }
    
    fun updateUserProfile(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.updateUserProfile(user)
                .onSuccess {
                    _currentUser.value = user
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Profile update failed"
                }
            
            _isLoading.value = false
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
