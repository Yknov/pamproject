package yk.example.pamproject.data.profile

import yk.example.pamproject.domain.profile.model.UserProfile
import yk.example.pamproject.domain.profile.repository.ProfileRepository

class DummyProfileRepository : ProfileRepository {

    override suspend fun getProfile(email: String): Result<UserProfile> = runCatching {
        profiles.getOrPut(email) {
            UserProfile(
                email = email,
                name = email.substringBefore('@').ifBlank { "Pengguna" },
                phone = "",
            )
        }
    }

    override suspend fun updateProfile(profile: UserProfile): Result<Unit> = runCatching {
        profiles[profile.email] = profile
    }

    companion object {
        private val profiles = mutableMapOf<String, UserProfile>()
    }
}
