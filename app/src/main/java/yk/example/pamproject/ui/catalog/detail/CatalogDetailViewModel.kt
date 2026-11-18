package yk.example.pamproject.ui.catalog.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.catalog.DummyCatalogRepository
import yk.example.pamproject.data.reviews.DummyReviewRepository
import yk.example.pamproject.domain.catalog.repository.CatalogRepository
import yk.example.pamproject.domain.reviews.repository.ReviewRepository

class CatalogDetailViewModel(
    private val catalogRepository: CatalogRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    constructor() : this(DummyCatalogRepository(), DummyReviewRepository())

    private val _uiState = MutableStateFlow(CatalogDetailUiState())
    val uiState: StateFlow<CatalogDetailUiState> = _uiState.asStateFlow()

    private var loadedMenuName: String? = null

    fun load(menuName: String) {
        if (menuName.isBlank() || loadedMenuName == menuName) return
        loadedMenuName = menuName
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val menuResult = catalogRepository.getMenuByName(menuName)
            val reviewsResult = reviewRepository.getReviews(menuName)
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    menu = menuResult.getOrNull(),
                    reviews = reviewsResult.getOrDefault(emptyList()),
                    errorMessage = menuResult.exceptionOrNull()?.message
                        ?: reviewsResult.exceptionOrNull()?.message,
                )
            }
        }
    }
}
