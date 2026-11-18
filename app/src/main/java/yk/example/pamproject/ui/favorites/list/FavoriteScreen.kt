package yk.example.pamproject.ui.favorites.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.SectionHeading
import yk.example.pamproject.ui.favorites.FavoriteUiState
import yk.example.pamproject.ui.favorites.components.FavoriteGridCard

/**
 * Screen presenting saved favorite menus in a 2-column [LazyVerticalGrid] with category filters
 * and remove actions.
 */
@Composable
fun FavoriteScreen(
    uiState: FavoriteUiState,
    navItems: List<CanteenNavItem>,
    selectedDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onSelectCategory: (String) -> Unit,
    onOpenFavorite: (String) -> Unit,
    onRemoveFavorite: (String) -> Unit,
) {
    val filteredFavorites = uiState.favorites.filter { item ->
        uiState.selectedCategory == "Semua" ||
            item.category.equals(uiState.selectedCategory, ignoreCase = true)
    }

    CanteenScaffold(
        title = "Menu favorit",
        navItems = navItems,
        selectedDestination = selectedDestination,
        onNavigate = onNavigate,
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Semua", "Makanan", "Minuman").forEach { category ->
                        FilterChip(
                            selected = uiState.selectedCategory == category,
                            onClick = { onSelectCategory(category) },
                            label = { Text(category) },
                            shape = RoundedCornerShape(50),
                        )
                    }
                }
            }

            uiState.statusMessage?.let { message ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    InfoPanel(
                        title = "Pembaruan favorit",
                        message = message,
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeading(title = "Menu tersimpan (${filteredFavorites.size})")
            }

            if (filteredFavorites.isNotEmpty()) {
                items(
                    items = filteredFavorites,
                    key = { it.id },
                ) { favorite ->
                    FavoriteGridCard(
                        item = favorite,
                        onClick = { onOpenFavorite(favorite.menuName) },
                        onRemove = { onRemoveFavorite(favorite.menuName) },
                    )
                }
            } else {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    InfoPanel(
                        title = "Belum ada menu favorit",
                        message = "Simpan menu favorit Anda dari halaman detail katalog.",
                    )
                }
            }
        }
    }
}
