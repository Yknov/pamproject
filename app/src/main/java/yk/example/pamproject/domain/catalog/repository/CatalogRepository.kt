package yk.example.pamproject.domain.catalog.repository

import yk.example.pamproject.domain.catalog.model.Menu

interface CatalogRepository {
    suspend fun getMenus(query: String = "", category: String = "Semua"): Result<List<Menu>>
    suspend fun getMenuByName(name: String): Result<Menu?>
    suspend fun saveMenu(menu: Menu): Result<Unit>
}
