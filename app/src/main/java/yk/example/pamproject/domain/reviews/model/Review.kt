package yk.example.pamproject.domain.reviews.model

data class Review(
    val id: String,
    val dish: String,
    val author: String,
    val stars: Int,
    val comment: String,
)
