package yk.example.pamproject.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.profile.DummyProfileRepository
import yk.example.pamproject.domain.profile.repository.ProfileRepository

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    constructor() : this(DummyProfileRepository())

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var loadedEmail: String? = null

    fun load(email: String) {
        if (email.isBlank() || loadedEmail == email) return
        loadedEmail = email
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            profileRepository.getProfile(email)
                .onSuccess { profile ->
                    _uiState.update { it.copy(isLoading = false, profile = profile) }
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
}
