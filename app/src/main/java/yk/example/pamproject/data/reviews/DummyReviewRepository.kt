package yk.example.pamproject.data.reviews

import yk.example.pamproject.domain.reviews.model.Review
import yk.example.pamproject.domain.reviews.repository.ReviewRepository

class DummyReviewRepository : ReviewRepository {

    override suspend fun getReviews(menuName: String): Result<List<Review>> = runCatching {
        if (menuName.isBlank()) dummyReviews else dummyReviews.filter { it.dish == menuName }
    }

    override suspend fun getReviewByDish(dish: String): Result<Review?> = runCatching {
        dummyReviews.firstOrNull { it.dish == dish } ?: dummyReviews.firstOrNull()
    }

    override suspend fun submitReview(review: Review): Result<Unit> = runCatching {
        Unit
    }

    companion object {
        val dummyReviews: List<Review> = listOf(
            Review(
                id = "1",
                dish = "Nasi ayam sambal matah",
                author = "Pengguna CanteenKu",
                stars = 5,
                comment = "Sambalnya segar dan porsinya pas.",
            ),
            Review(
                id = "2",
                dish = "Mie ayam jamur",
                author = "Mahasiswa",
                stars = 4,
                comment = "Rasanya enak, pelayanannya cukup cepat.",
            ),
            Review(
                id = "3",
                dish = "Es teh kampus",
                author = "Mahasiswa",
                stars = 5,
                comment = "Manisnya pas dan segar saat cuaca panas.",
            ),
        )
    }
}
