package yk.example.pamproject.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.auth.DummyAuthRepository
import yk.example.pamproject.domain.auth.model.UserSession
import yk.example.pamproject.domain.auth.repository.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    constructor() : this(DummyAuthRepository())

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val state = _uiState.value
        if (!state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            authRepository.signIn(state.email, state.password)
                .onSuccess { session ->
                    _uiState.update { it.copy(isSubmitting = false) }
                    _effect.send(LoginEffect.NavigateToHome(session))
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = error.message ?: "Gagal masuk. Coba lagi.",
                        )
                    }
                }
        }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            _effect.send(LoginEffect.NavigateToRegister)
        }
    }
}

sealed interface LoginEffect {
    data class NavigateToHome(val session: UserSession) : LoginEffect
    data object NavigateToRegister : LoginEffect
}
