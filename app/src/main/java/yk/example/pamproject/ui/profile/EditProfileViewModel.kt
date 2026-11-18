package yk.example.pamproject.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.profile.DummyProfileRepository
import yk.example.pamproject.domain.profile.model.UserProfile
import yk.example.pamproject.domain.profile.repository.ProfileRepository

class EditProfileViewModel(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    constructor() : this(DummyProfileRepository())

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _effect = Channel<EditProfileEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var loadedEmail: String? = null

    fun load(email: String) {
        if (email.isBlank() || loadedEmail == email) return
        loadedEmail = email
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, email = email) }
            profileRepository.getProfile(email)
                .onSuccess { profile ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            email = profile.email,
                            name = profile.name,
                            phone = profile.phone,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal memuat profil.",
                        )
                    }
                }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, errorMessage = null) }
    }

    fun onSaveClick() {
        val state = _uiState.value
        if (!state.canSave) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            profileRepository.updateProfile(
                UserProfile(
                    email = state.email,
                    name = state.name.trim(),
                    phone = state.phone.trim(),
                ),
            ).onSuccess {
                _uiState.update { it.copy(isSaving = false) }
                _effect.send(EditProfileEffect.Saved)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Gagal menyimpan profil.",
                    )
                }
            }
        }
    }
}

sealed interface EditProfileEffect {
    data object Saved : EditProfileEffect
}
