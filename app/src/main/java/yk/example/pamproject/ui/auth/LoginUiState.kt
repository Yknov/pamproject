package yk.example.pamproject.ui.auth

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSubmit: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isSubmitting
}
