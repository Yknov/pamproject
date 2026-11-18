package yk.example.pamproject.ui.reviews.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.reviews.DummyReviewRepository
import yk.example.pamproject.domain.reviews.repository.ReviewRepository

class ReviewListViewModel(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    constructor() : this(DummyReviewRepository())

    private val _uiState = MutableStateFlow(ReviewListUiState())
    val uiState: StateFlow<ReviewListUiState> = _uiState.asStateFlow()

    private var loadedMenuName: String? = null

    fun load(menuName: String) {
        if (loadedMenuName == menuName) return
        loadedMenuName = menuName
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, menuName = menuName) }
            reviewRepository.getReviews(menuName)
                .onSuccess { reviews ->
                    _uiState.update { it.copy(isLoading = false, reviews = reviews) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal memuat ulasan.",
                        )
                    }
                }
        }
    }
}
