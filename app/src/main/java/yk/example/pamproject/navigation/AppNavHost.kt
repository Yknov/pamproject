package yk.example.pamproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yk.example.pamproject.ui.auth.LoginScreen
import yk.example.pamproject.ui.auth.RegisterScreen
import yk.example.pamproject.ui.catalog.CatalogDetailScreen
import yk.example.pamproject.ui.catalog.CatalogFormScreen
import yk.example.pamproject.ui.catalog.CatalogListScreen
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.home.admin.AdminHomeScreen
import yk.example.pamproject.ui.home.user.UserHomeScreen
import yk.example.pamproject.ui.profile.EditProfileScreen
import yk.example.pamproject.ui.profile.ProfileScreen
import yk.example.pamproject.ui.reviews.ReviewDetailScreen
import yk.example.pamproject.ui.reviews.ReviewFormScreen
import yk.example.pamproject.ui.reviews.ReviewListScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    var activeEmail by rememberSaveable { mutableStateOf("") }
    var activeName by rememberSaveable { mutableStateOf("") }
    var isAdmin by rememberSaveable { mutableStateOf(false) }
    var selectedItem by rememberSaveable { mutableStateOf("") }
    var editing by rememberSaveable { mutableStateOf(false) }

    val homeDestination = if (isAdmin) AppDestination.AdminHome else AppDestination.UserHome
    val userNav = listOf(
        CanteenNavItem("Beranda", AppDestination.UserHome),
        CanteenNavItem("Menu", AppDestination.Catalog),
        CanteenNavItem("Ulasan", AppDestination.Reviews),
        CanteenNavItem("Profil", AppDestination.Profile),
    )
    val adminNav = listOf(
        CanteenNavItem("Dashboard", AppDestination.AdminHome),
        CanteenNavItem("Menu", AppDestination.Catalog),
        CanteenNavItem("Ulasan", AppDestination.Reviews),
        CanteenNavItem("Profil", AppDestination.Profile),
    )
    val navItems = if (isAdmin) adminNav else userNav
    val navigate: (AppDestination) -> Unit = { target ->
        if (target == AppDestination.Reviews) {
            selectedItem = ""
        }
        navController.navigate(target.name) {
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.name,
    ) {
        composable(AppDestination.Login.name) {
            LoginScreen(
                onLogin = { email ->
                    activeEmail = email.trim()
                    activeName = email.substringBefore('@').ifBlank { "Pengguna" }
                    isAdmin = email.substringBefore('@').equals("admin", ignoreCase = true)
                    val target = if (isAdmin) AppDestination.AdminHome else AppDestination.UserHome
                    navController.navigate(target.name) {
                        popUpTo(AppDestination.Login.name) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegister = {
                    navController.navigate(AppDestination.Register.name) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(AppDestination.Register.name) {
            RegisterScreen(
                onRegister = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Login.name) {
                            launchSingleTop = true
                        }
                    }
                },
                onLogin = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Login.name) {
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
        composable(AppDestination.UserHome.name) {
            UserHomeScreen(
                name = activeName,
                navItems = userNav,
                onNavigate = navigate,
                onOpenMenu = {
                    selectedItem = it
                    navController.navigate(AppDestination.CatalogDetail.name)
                },
            )
        }
        composable(AppDestination.AdminHome.name) {
            AdminHomeScreen(
                name = activeName,
                navItems = adminNav,
                onNavigate = navigate,
            )
        }
        composable(AppDestination.Profile.name) {
            ProfileScreen(
                email = activeEmail,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(homeDestination.name) {
                            launchSingleTop = true
                        }
                    }
                },
                onEditProfile = {
                    navController.navigate(AppDestination.EditProfile.name)
                },
                onLogout = {
                    navController.navigate(AppDestination.Login.name) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(AppDestination.EditProfile.name) {
            EditProfileScreen(
                email = activeEmail,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Profile.name)
                    }
                },
                onSave = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Profile.name)
                    }
                },
            )
        }
        composable(AppDestination.Catalog.name) {
            CatalogListScreen(
                isAdmin = isAdmin,
                navItems = navItems,
                selectedDestination = AppDestination.Catalog,
                onNavigate = navigate,
                onOpenMenu = {
                    selectedItem = it
                    navController.navigate(AppDestination.CatalogDetail.name)
                },
                onAddMenu = {
                    editing = false
                    navController.navigate(AppDestination.CatalogForm.name)
                },
            )
        }
        composable(AppDestination.CatalogDetail.name) {
            CatalogDetailScreen(
                menuName = selectedItem,
                isAdmin = isAdmin,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Catalog.name)
                    }
                },
                onReviews = {
                    navController.navigate(AppDestination.Reviews.name)
                },
                onEdit = {
                    editing = true
                    navController.navigate(AppDestination.CatalogForm.name)
                },
            )
        }
        composable(AppDestination.CatalogForm.name) {
            val fallbackDestination = if (editing) AppDestination.CatalogDetail else AppDestination.Catalog
            CatalogFormScreen(
                isEditing = editing,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(fallbackDestination.name)
                    }
                },
                onSave = {
                    if (!navController.popBackStack()) {
                        navController.navigate(fallbackDestination.name)
                    }
                },
            )
        }
        composable(AppDestination.Reviews.name) {
            ReviewListScreen(
                menuName = selectedItem,
                navItems = navItems,
                selectedDestination = AppDestination.Reviews,
                onNavigate = navigate,
                onBack = if (selectedItem.isNotBlank()) {
                    {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppDestination.CatalogDetail.name)
                        }
                    }
                } else {
                    null
                },
                onOpenReview = {
                    selectedItem = it
                    navController.navigate(AppDestination.ReviewDetail.name)
                },
                onWriteReview = {
                    if (selectedItem.isBlank()) selectedItem = "Nasi ayam sambal matah"
                    navController.navigate(AppDestination.ReviewForm.name)
                },
            )
        }
        composable(AppDestination.ReviewDetail.name) {
            ReviewDetailScreen(
                dish = selectedItem,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Reviews.name)
                    }
                },
                onEdit = {
                    navController.navigate(AppDestination.ReviewForm.name)
                },
            )
        }
        composable(AppDestination.ReviewForm.name) {
            ReviewFormScreen(
                dish = selectedItem.ifBlank { "Nasi ayam sambal matah" },
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Reviews.name)
                    }
                },
                onSave = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Reviews.name)
                    }
                },
            )
        }
    }
}
