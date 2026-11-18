package yk.example.pamproject.ui.favorites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import yk.example.pamproject.data.favorites.InMemoryFavoriteRepository
import yk.example.pamproject.domain.favorites.model.FavoriteMenu
import yk.example.pamproject.domain.favorites.repository.FavoriteRepository
import yk.example.pamproject.ui.catalog.DemoMenu

/**
 * UI state for the Favorites & Subscriptions module.
 */
data class FavoriteUiState(
    val favorites: List<FavoriteMenu> = emptyList(),
    val selectedCategory: String = "Semua",
    val reorderMessage: String? = null,
    val statusMessage: String? = null,
)

/**
 * ViewModel managing favorite menu state, category filtering, CRUD actions, and quick re-order.
 */
class FavoriteViewModel(
    private val repository: FavoriteRepository = InMemoryFavoriteRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FavoriteUiState(favorites = repository.getFavorites()),
    )
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun isFavorite(menuName: String): Boolean = repository.isFavorite(menuName)

    fun addFavorite(menu: DemoMenu) {
        repository.addFavorite(menu.toFavoriteMenu())
        _uiState.update {
            it.copy(
                favorites = repository.getFavorites(),
                statusMessage = "${menu.name} ditambahkan ke daftar favorit.",
            )
        }
    }

    fun removeFavorite(menuName: String) {
        repository.removeFavorite(menuName)
        _uiState.update {
            it.copy(
                favorites = repository.getFavorites(),
                statusMessage = "$menuName dihapus dari daftar favorit.",
                reorderMessage = null,
            )
        }
    }

    fun toggleFavorite(menu: DemoMenu) {
        if (isFavorite(menu.name)) {
            removeFavorite(menu.name)
        } else {
            addFavorite(menu)
        }
    }

    fun quickReorder(
        menuName: String,
        portionCount: Int? = null,
        orderType: String? = null,
    ) {
        val message = if (portionCount != null && orderType != null) {
            "Pesanan cepat $portionCount× $menuName ($orderType) disiapkan. Estimasi siap dalam 10 menit."
        } else {
            "Pesanan ulang untuk $menuName disiapkan. Estimasi siap dalam 10 menit."
        }
        _uiState.update {
            it.copy(reorderMessage = message)
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                reorderMessage = null,
                statusMessage = null,
            )
        }
    }

    private fun DemoMenu.toFavoriteMenu(): FavoriteMenu = FavoriteMenu(
        id = "fav-${name.lowercase().replace(" ", "-")}",
        menuName = name,
        standName = stand,
        price = price,
        category = category,
        description = "Menu favorit dari $stand ($category). Siap dipesan kembali dengan cepat.",
        isSubscribedStand = true,
    )
}
