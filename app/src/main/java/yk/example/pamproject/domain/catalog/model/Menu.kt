package yk.example.pamproject.domain.catalog.model

data class Menu(
    val id: String,
    val name: String,
    val stand: String,
    val price: String,
    val category: String,
    val description: String = "",
    val imageUri: String? = null,
)
