package com.dcc.eventticketapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthViewState())
    val state: StateFlow<AuthViewState> = _state

    fun handleIntent(intent: AuthIntent) {
        when (intent) {

            // Login
            is AuthIntent.LoginEmailChanged -> {
                _state.value = _state.value.copy(loginEmail = intent.email)
            }
            is AuthIntent.LoginPasswordChanged -> {
                _state.value = _state.value.copy(loginPassword = intent.password)
            }
            is AuthIntent.SubmitLogin -> {
                viewModelScope.launch { submitLogin() }
            }

            // Register
            is AuthIntent.RegisterNameChanged -> {
                _state.value = _state.value.copy(registerName = intent.name)
            }
            is AuthIntent.RegisterEmailChanged -> {
                val isError = intent.email.isNotEmpty() &&
                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(intent.email).matches()
                _state.value = _state.value.copy(
                    registerEmail = intent.email,
                    emailError    = isError
                )
            }
            is AuthIntent.RegisterPhoneChanged -> {
                val isError = intent.phone.isNotEmpty() &&
                        intent.phone.length < 10
                _state.value = _state.value.copy(
                    registerPhone = intent.phone,
                    phoneError    = isError
                )
            }
            is AuthIntent.RegisterPasswordChanged -> {
                val matchError =
                    _state.value.registerConfirmPassword.isNotEmpty() &&
                            intent.password != _state.value.registerConfirmPassword
                _state.value = _state.value.copy(
                    registerPassword   = intent.password,
                    passwordMatchError = matchError
                )
            }
            is AuthIntent.RegisterConfirmPasswordChanged -> {
                val matchError = intent.confirmPassword.isNotEmpty() &&
                        intent.confirmPassword != _state.value.registerPassword
                _state.value = _state.value.copy(
                    registerConfirmPassword = intent.confirmPassword,
                    passwordMatchError      = matchError
                )
            }
            is AuthIntent.RegisterTermsChanged -> {
                _state.value = _state.value.copy(
                    registerTermsAccepted = intent.accepted
                )
            }
            is AuthIntent.SubmitRegister -> {
                viewModelScope.launch { submitRegister() }
            }

            // Commun
            is AuthIntent.ResetState -> {
                _state.value = AuthViewState()
            }
        }
    }

    private suspend fun submitLogin() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val user = repository.login(
                email    = _state.value.loginEmail,
                password = _state.value.loginPassword
            )
            _state.value = _state.value.copy(
                isLoading       = false,
                isSuccess       = true,
                isAuthenticated = true,
                user            = user
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error     = e.message ?: "Erreur de connexion"
            )
        }
    }

    private suspend fun submitRegister() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val user = repository.register(
                fullName = _state.value.registerName,
                email    = _state.value.registerEmail,
                phone    = _state.value.registerPhone,
                password = _state.value.registerPassword
            )
            _state.value = _state.value.copy(
                isLoading       = false,
                isSuccess       = true,
                isAuthenticated = true,
                user            = user
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error     = e.message ?: "Erreur d'inscription"
            )
        }
    }
}