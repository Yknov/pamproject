package yk.example.pamproject.ui.reviews.form

data class ReviewFormUiState(
    val dish: String = "Nasi ayam sambal matah",
    val rating: Int = 5,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSubmit: Boolean
        get() = !isSubmitting
}
