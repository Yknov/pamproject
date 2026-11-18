package yk.example.pamproject.ui.profile

data class EditProfileUiState(
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSave: Boolean
        get() = name.isNotBlank() && !isSaving && !isLoading
}
