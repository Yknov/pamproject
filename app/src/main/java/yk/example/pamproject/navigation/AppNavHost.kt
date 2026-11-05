package yk.example.pamproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import yk.example.pamproject.ui.auth.LoginScreen
import yk.example.pamproject.ui.auth.RegisterScreen
import yk.example.pamproject.ui.profile.ProfileScreen
import yk.example.pamproject.ui.home.admin.AdminHomeScreen
import yk.example.pamproject.ui.home.user.UserHomeScreen

private enum class Route { Login, Register, UserHome, AdminHome, Profile }

@Composable
fun AppNavHost() {
    var route by remember { mutableStateOf(Route.Login.name) }
    var activeName by remember { mutableStateOf("") }
    when (Route.valueOf(route)) {
        Route.Login -> LoginScreen(
            onLogin = { email ->
                activeName = email.substringBefore('@').ifBlank { "Pengguna" }
                route = if (email.substringBefore('@').equals("admin", ignoreCase = true)) {
                    Route.AdminHome.name
                } else {
                    Route.UserHome.name
                }
            },
            onRegister = { route = Route.Register.name },
        )
        Route.Register -> RegisterScreen(
            onRegister = { route = Route.Login.name },
            onLogin = { route = Route.Login.name },
        )
        Route.UserHome -> UserHomeScreen(
            name = activeName,
            onProfile = { route = Route.Profile.name },
            onLogout = { route = Route.Login.name },
        )
        Route.AdminHome -> AdminHomeScreen(
            name = activeName,
            onProfile = { route = Route.Profile.name },
            onLogout = { route = Route.Login.name },
        )
        Route.Profile -> ProfileScreen(onLogout = { route = Route.Login.name })
    }
}
