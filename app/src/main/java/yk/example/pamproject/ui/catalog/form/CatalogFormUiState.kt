package yk.example.pamproject.ui.catalog.form

data class CatalogFormUiState(
    val isEditing: Boolean = false,
    val menuId: String? = null,
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val category: String = "Makanan",
    val stand: String = "",
    val imageUri: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val imageError: String? = null,
) {
    val canSave: Boolean
        get() = name.isNotBlank() && price.isNotBlank() && !isSaving && !isLoading
}
