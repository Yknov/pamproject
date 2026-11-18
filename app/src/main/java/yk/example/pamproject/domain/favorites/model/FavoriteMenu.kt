package yk.example.pamproject.domain.favorites.model

/**
 * Domain model representing a user's favorite menu item from a canteen stand.
 */
data class FavoriteMenu(
    val id: String,
    val menuName: String,
    val standName: String,
    val price: String,
    val category: String,
    val description: String,
    val isSubscribedStand: Boolean = true,
)
