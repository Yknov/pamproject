package yk.example.pamproject.domain.auth.repository

import yk.example.pamproject.domain.auth.model.UserSession

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<UserSession>
    suspend fun signUp(name: String, email: String, password: String): Result<Unit>
}
