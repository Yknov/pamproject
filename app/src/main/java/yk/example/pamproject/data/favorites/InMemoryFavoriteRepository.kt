package yk.example.pamproject.data.favorites

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import yk.example.pamproject.domain.favorites.model.FavoriteMenu
import yk.example.pamproject.domain.favorites.repository.FavoriteRepository

/**
 * In-memory implementation of [FavoriteRepository] supporting Read, Insert, and Delete operations
 * before Supabase backend integration.
 */
class InMemoryFavoriteRepository(
    initialFavorites: List<FavoriteMenu> = defaultSeedFavorites(),
) : FavoriteRepository {

    private val _favorites = MutableStateFlow(initialFavorites)
    override val favorites: StateFlow<List<FavoriteMenu>> = _favorites.asStateFlow()

    override fun getFavorites(): List<FavoriteMenu> = _favorites.value

    override fun addFavorite(menu: FavoriteMenu) {
        _favorites.update { current ->
            if (current.any { it.menuName.equals(menu.menuName, ignoreCase = true) }) {
                current
            } else {
                current + menu
            }
        }
    }

    override fun removeFavorite(menuName: String) {
        _favorites.update { current ->
            current.filterNot { it.menuName.equals(menuName, ignoreCase = true) }
        }
    }

    override fun isFavorite(menuName: String): Boolean =
        _favorites.value.any { it.menuName.equals(menuName, ignoreCase = true) }

    companion object {
        fun defaultSeedFavorites(): List<FavoriteMenu> = listOf(
            FavoriteMenu(
                id = "fav-1",
                menuName = "Nasi ayam sambal matah",
                standName = "Warung Bu Rini",
                price = "Rp18.000",
                category = "Makanan",
                description = "Nasi hangat dengan ayam suwir bumbu sambal matah segar khas kantin kampus.",
                isSubscribedStand = true,
            ),
            FavoriteMenu(
                id = "fav-2",
                menuName = "Es teh kampus",
                standName = "Kedai Segar",
                price = "Rp5.000",
                category = "Minuman",
                description = "Es teh manis racikan segar untuk menemani waktu istirahat kuliah.",
                isSubscribedStand = true,
            ),
        )
    }
}
