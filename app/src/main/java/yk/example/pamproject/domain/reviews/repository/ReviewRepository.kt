package yk.example.pamproject.domain.reviews.repository

import yk.example.pamproject.domain.reviews.model.Review

interface ReviewRepository {
    suspend fun getReviews(menuName: String = ""): Result<List<Review>>
    suspend fun getReviewByDish(dish: String): Result<Review?>
    suspend fun submitReview(review: Review): Result<Unit>
}
