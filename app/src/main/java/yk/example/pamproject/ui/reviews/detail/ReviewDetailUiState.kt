package yk.example.pamproject.ui.reviews.detail

import yk.example.pamproject.domain.reviews.model.Review

data class ReviewDetailUiState(
    val review: Review? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
