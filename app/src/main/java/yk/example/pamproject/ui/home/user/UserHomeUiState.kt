package yk.example.pamproject.ui.home.user

import yk.example.pamproject.domain.catalog.model.Menu
import yk.example.pamproject.domain.reviews.model.Review

data class UserHomeUiState(
    val displayName: String = "",
    val menus: List<Menu> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val menuCount: Int get() = menus.size
    val reviewCount: Int get() = reviews.size
    val averageRating: Double
        get() = if (reviews.isEmpty()) 0.0 else reviews.map { it.stars }.average()
}
