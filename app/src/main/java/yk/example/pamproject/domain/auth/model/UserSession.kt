package yk.example.pamproject.domain.auth.model

data class UserSession(
    val email: String,
    val name: String,
    val isAdmin: Boolean,
)
