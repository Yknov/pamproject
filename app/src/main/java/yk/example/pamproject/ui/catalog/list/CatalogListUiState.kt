package yk.example.pamproject.ui.catalog.list

import yk.example.pamproject.domain.catalog.model.Menu

data class CatalogListUiState(
    val query: String = "",
    val selectedCategory: String = "Semua",
    val menus: List<Menu> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val categories: List<String> = listOf("Semua", "Makanan", "Minuman")
}
