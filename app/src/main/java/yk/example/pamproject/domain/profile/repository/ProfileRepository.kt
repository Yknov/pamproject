package yk.example.pamproject.domain.profile.repository

import yk.example.pamproject.domain.profile.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(email: String): Result<UserProfile>
    suspend fun updateProfile(profile: UserProfile): Result<Unit>
}
