package yk.example.pamproject.ui.reviews.list

import yk.example.pamproject.domain.reviews.model.Review

data class ReviewListUiState(
    val menuName: String = "",
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
