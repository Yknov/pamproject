package yk.example.pamproject

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import yk.example.pamproject.data.favorites.InMemoryFavoriteRepository
import yk.example.pamproject.domain.favorites.model.FavoriteMenu
import yk.example.pamproject.ui.catalog.DemoMenu
import yk.example.pamproject.ui.favorites.FavoriteViewModel

class FavoriteViewModelTest {

    @Test
    fun repository_readInsertDelete_operatesCorrectly() {
        val repo = InMemoryFavoriteRepository()
        val initialCount = repo.getFavorites().size
        assertEquals(2, initialCount)
        assertTrue(repo.isFavorite("Nasi ayam sambal matah"))

        val newMenu = FavoriteMenu(
            id = "fav-mie",
            menuName = "Mie ayam jamur",
            standName = "Mie Pak Darto",
            price = "Rp15.000",
            category = "Makanan",
            description = "Mie ayam hangat",
        )
        repo.addFavorite(newMenu)
        assertEquals(3, repo.getFavorites().size)
        assertTrue(repo.isFavorite("Mie ayam jamur"))

        // Duplicate insert should not duplicate entries
        repo.addFavorite(newMenu)
        assertEquals(3, repo.getFavorites().size)

        repo.removeFavorite("Mie ayam jamur")
        assertEquals(2, repo.getFavorites().size)
        assertFalse(repo.isFavorite("Mie ayam jamur"))
    }

    @Test
    fun viewModel_toggleAndQuickReorder_updatesUiState() {
        val viewModel = FavoriteViewModel(InMemoryFavoriteRepository())
        val menu = DemoMenu("Mie ayam jamur", "Mie Pak Darto", "Rp15.000", "Makanan")

        assertFalse(viewModel.isFavorite(menu.name))
        viewModel.toggleFavorite(menu)
        assertTrue(viewModel.isFavorite(menu.name))
        assertEquals(3, viewModel.uiState.value.favorites.size)

        viewModel.selectCategory("Minuman")
        assertEquals("Minuman", viewModel.uiState.value.selectedCategory)

        viewModel.quickReorder(menu.name, portionCount = 2, orderType = "Dine-in")
        assertNotNull(viewModel.uiState.value.reorderMessage)
        assertTrue(viewModel.uiState.value.reorderMessage!!.contains("2× Mie ayam jamur (Dine-in)"))

        viewModel.toggleFavorite(menu)
        assertFalse(viewModel.isFavorite(menu.name))
        assertNull(viewModel.uiState.value.reorderMessage)
    }
}
