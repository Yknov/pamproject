package yk.example.pamproject.domain.favorites.repository

import kotlinx.coroutines.flow.StateFlow
import yk.example.pamproject.domain.favorites.model.FavoriteMenu

/**
 * Contract for managing the user's favorite menus (Read, Insert, Delete).
 */
interface FavoriteRepository {
    val favorites: StateFlow<List<FavoriteMenu>>

    fun getFavorites(): List<FavoriteMenu>

    fun addFavorite(menu: FavoriteMenu)

    fun removeFavorite(menuName: String)

    fun isFavorite(menuName: String): Boolean
}
