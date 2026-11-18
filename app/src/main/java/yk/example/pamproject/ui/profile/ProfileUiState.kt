package yk.example.pamproject.ui.profile

import yk.example.pamproject.domain.profile.model.UserProfile

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val email: String get() = profile?.email.orEmpty()
    val displayName: String
        get() = profile?.name?.ifBlank { null }
            ?: email.substringBefore('@').ifBlank { "Pengguna" }
    val initial: String
        get() = displayName.take(1).uppercase().ifBlank { "C" }
}
