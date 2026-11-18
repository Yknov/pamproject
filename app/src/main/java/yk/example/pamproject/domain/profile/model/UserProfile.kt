package yk.example.pamproject.domain.profile.model

data class UserProfile(
    val email: String,
    val name: String,
    val phone: String = "",
)
