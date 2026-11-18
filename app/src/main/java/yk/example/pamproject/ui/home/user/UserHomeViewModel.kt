package yk.example.pamproject.ui.home.user

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

class UserHomeViewModel(
    private val catalogRepository: CatalogRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    constructor() : this(DummyCatalogRepository(), DummyReviewRepository())

    private val _uiState = MutableStateFlow(UserHomeUiState())
    val uiState: StateFlow<UserHomeUiState> = _uiState.asStateFlow()

    fun setDisplayName(name: String) {
        _uiState.update { it.copy(displayName = name) }
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val menusResult = catalogRepository.getMenus()
            val reviewsResult = reviewRepository.getReviews()
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    menus = menusResult.getOrDefault(emptyList()),
                    reviews = reviewsResult.getOrDefault(emptyList()),
                    errorMessage = menusResult.exceptionOrNull()?.message
                        ?: reviewsResult.exceptionOrNull()?.message,
                )
            }
        }
    }
}
