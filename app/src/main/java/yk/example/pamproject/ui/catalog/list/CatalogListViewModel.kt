package yk.example.pamproject.ui.catalog.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.catalog.DummyCatalogRepository
import yk.example.pamproject.domain.catalog.repository.CatalogRepository

class CatalogListViewModel(
    private val catalogRepository: CatalogRepository,
) : ViewModel() {

    constructor() : this(DummyCatalogRepository())

    private val _uiState = MutableStateFlow(CatalogListUiState())
    val uiState: StateFlow<CatalogListUiState> = _uiState.asStateFlow()

    fun load() {
        fetchMenus()
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
        fetchMenus()
    }

    fun onCategoryChange(value: String) {
        _uiState.update { it.copy(selectedCategory = value) }
        fetchMenus()
    }

    private fun fetchMenus() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            catalogRepository.getMenus(state.query, state.selectedCategory)
                .onSuccess { menus ->
                    _uiState.update { it.copy(isLoading = false, menus = menus) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal memuat menu.",
                        )
                    }
                }
        }
    }
}
