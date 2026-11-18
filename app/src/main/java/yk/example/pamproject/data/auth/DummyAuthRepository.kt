package yk.example.pamproject.data.auth

import yk.example.pamproject.domain.auth.model.UserSession
import yk.example.pamproject.domain.auth.repository.AuthRepository

class DummyAuthRepository : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<UserSession> = runCatching {
        val trimmed = email.trim()
        val isAdmin = trimmed.substringBefore('@').equals("admin", ignoreCase = true)
        UserSession(
            email = trimmed,
            name = trimmed.substringBefore('@').ifBlank { "Pengguna" },
            isAdmin = isAdmin,
        )
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> = runCatching {
        Unit
    }
}
