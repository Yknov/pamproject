package yk.example.pamproject.ui.reviews.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.reviews.DummyReviewRepository
import yk.example.pamproject.domain.reviews.repository.ReviewRepository

class ReviewDetailViewModel(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    constructor() : this(DummyReviewRepository())

    private val _uiState = MutableStateFlow(ReviewDetailUiState())
    val uiState: StateFlow<ReviewDetailUiState> = _uiState.asStateFlow()

    private var loadedDish: String? = null

    fun load(dish: String) {
        if (dish.isBlank() || loadedDish == dish) return
        loadedDish = dish
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            reviewRepository.getReviewByDish(dish)
                .onSuccess { review ->
                    _uiState.update { it.copy(isLoading = false, review = review) }
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
