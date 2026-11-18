package yk.example.pamproject.ui.reviews.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.reviews.DummyReviewRepository
import yk.example.pamproject.domain.reviews.model.Review
import yk.example.pamproject.domain.reviews.repository.ReviewRepository

class ReviewFormViewModel(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    constructor() : this(DummyReviewRepository())

    private val _uiState = MutableStateFlow(ReviewFormUiState())
    val uiState: StateFlow<ReviewFormUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ReviewFormEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun setDish(dish: String) {
        val resolved = dish.ifBlank { "Nasi ayam sambal matah" }
        _uiState.update { it.copy(dish = resolved) }
    }

    fun onRatingChange(value: Int) {
        _uiState.update { it.copy(rating = value) }
    }

    fun onCommentChange(value: String) {
        _uiState.update { it.copy(comment = value) }
    }

    fun onSubmitClick() {
        val state = _uiState.value
        if (!state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            reviewRepository.submitReview(
                Review(
                    id = "",
                    dish = state.dish,
                    author = "Pengguna CanteenKu",
                    stars = state.rating,
                    comment = state.comment.trim(),
                ),
            ).onSuccess {
                _uiState.update { it.copy(isSubmitting = false) }
                _effect.send(ReviewFormEffect.Submitted)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Gagal mengirim ulasan.",
                    )
                }
            }
        }
    }
}

sealed interface ReviewFormEffect {
    data object Submitted : ReviewFormEffect
}
