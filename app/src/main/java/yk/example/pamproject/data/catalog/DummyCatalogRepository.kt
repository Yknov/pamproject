package yk.example.pamproject.data.catalog

import yk.example.pamproject.domain.catalog.model.Menu
import yk.example.pamproject.domain.catalog.repository.CatalogRepository

class DummyCatalogRepository : CatalogRepository {

    override suspend fun getMenus(query: String, category: String): Result<List<Menu>> = runCatching {
        dummyMenus.filter { menu ->
            (category == "Semua" || menu.category == category) &&
                (
                    query.isBlank() ||
                        menu.name.contains(query, ignoreCase = true) ||
                        menu.stand.contains(query, ignoreCase = true)
                    )
        }
    }

    override suspend fun getMenuByName(name: String): Result<Menu?> = runCatching {
        dummyMenus.firstOrNull { it.name == name } ?: dummyMenus.firstOrNull()
    }

    override suspend fun saveMenu(menu: Menu): Result<Unit> = runCatching {
        Unit
    }

    companion object {
        val dummyMenus: List<Menu> = listOf(
            Menu(
                id = "1",
                name = "Nasi ayam sambal matah",
                stand = "Warung Bu Rini",
                price = "Rp18.000",
                category = "Makanan",
                imageUri = null,
            ),
            Menu(
                id = "2",
                name = "Mie ayam jamur",
                stand = "Mie Pak Darto",
                price = "Rp15.000",
                category = "Makanan",
                imageUri = null,
            ),
            Menu(
                id = "3",
                name = "Es teh kampus",
                stand = "Kedai Segar",
                price = "Rp5.000",
                category = "Minuman",
                imageUri = null,
            ),
        )
    }
}
