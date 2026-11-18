package yk.example.pamproject.ui.catalog.detail

import yk.example.pamproject.domain.catalog.model.Menu
import yk.example.pamproject.domain.reviews.model.Review

data class CatalogDetailUiState(
    val menu: Menu? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val averageRating: Double
        get() = if (reviews.isEmpty()) 0.0 else reviews.map { it.stars }.average()
}
