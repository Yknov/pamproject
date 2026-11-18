
package yk.example.pamproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import yk.example.pamproject.ui.auth.LoginScreen
import yk.example.pamproject.ui.auth.RegisterScreen

import yk.example.pamproject.ui.canteen_management.StandDetailScreen
import yk.example.pamproject.ui.canteen_management.StandFormScreen
import yk.example.pamproject.ui.canteen_management.StandListScreen

import yk.example.pamproject.ui.canteen_pay.TopUpScreen
import yk.example.pamproject.ui.canteen_pay.WalletScreen

import yk.example.pamproject.ui.catalog.CatalogDetailScreen
import yk.example.pamproject.ui.catalog.CatalogFormScreen
import yk.example.pamproject.ui.catalog.CatalogListScreen

import yk.example.pamproject.ui.components.CanteenNavItem

import yk.example.pamproject.ui.favorites.FavoritesScreen
import yk.example.pamproject.ui.favorites.SubscriptionScreen

import yk.example.pamproject.ui.home.admin.AdminHomeScreen
import yk.example.pamproject.ui.home.user.UserHomeScreen

import yk.example.pamproject.ui.ordering.OrderDetailScreen
import yk.example.pamproject.ui.ordering.OrderFormScreen
import yk.example.pamproject.ui.ordering.OrderListScreen
import yk.example.pamproject.ui.ordering.OrderingViewModel

import yk.example.pamproject.ui.profile.EditProfileScreen
import yk.example.pamproject.ui.profile.ProfileScreen

import yk.example.pamproject.ui.reviews.ReviewDetailScreen
import yk.example.pamproject.ui.reviews.ReviewFormScreen
import yk.example.pamproject.ui.reviews.ReviewListScreen

@Composable
fun AppNavHost() {

    var destination by remember {
        mutableStateOf(AppDestination.Login)
    }

    var activeEmail by remember { mutableStateOf("") }
    var activeName by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }
    var editing by remember { mutableStateOf(false) }

    // Ordering ViewModel
    val orderingViewModel = remember {
        OrderingViewModel()
    }

    val homeDestination =
        if (isAdmin) AppDestination.AdminHome
        else AppDestination.UserHome

    // Navigasi pengguna
    val userNav = listOf(
        CanteenNavItem("Beranda", AppDestination.UserHome),
        CanteenNavItem("Menu", AppDestination.Catalog),
        CanteenNavItem("Pesanan", AppDestination.Orders),
        CanteenNavItem("Favorit", AppDestination.Favorites),
        CanteenNavItem("Profil", AppDestination.Profile),
    )

    val favoritesNav = listOf(
        CanteenNavItem("Beranda", AppDestination.UserHome),
        CanteenNavItem("Menu", AppDestination.Catalog),
        CanteenNavItem("Pesanan", AppDestination.Orders),
        CanteenNavItem("Favorit", AppDestination.Favorites),
        CanteenNavItem("Profil", AppDestination.Profile),
    )

    val walletNav = listOf(
        CanteenNavItem("Beranda", AppDestination.UserHome),
        CanteenNavItem("Menu", AppDestination.Catalog),
        CanteenNavItem("Pesanan", AppDestination.Orders),
        CanteenNavItem("Dompet", AppDestination.Wallet),
        CanteenNavItem("Profil", AppDestination.Profile),
    )

    // Navigasi admin
    val adminNav = listOf(
        CanteenNavItem("Dashboard", AppDestination.AdminHome),
        CanteenNavItem("Pesanan", AppDestination.Orders),
        CanteenNavItem("Menu", AppDestination.Catalog),
        CanteenNavItem("Stand", AppDestination.Stands),
        CanteenNavItem("Profil", AppDestination.Profile),
    )

    val navItems = if (isAdmin) adminNav else userNav

    val navigate: (AppDestination) -> Unit = {
        destination = it
    }

    when (destination) {

        // LOGIN
        AppDestination.Login -> LoginScreen(
            onLogin = { email ->

                activeEmail = email.trim()

                activeName = email.substringBefore("@")
                    .ifBlank { "Pengguna" }

                isAdmin = email.substringBefore("@")
                    .equals("admin", ignoreCase = true)

                destination =
                    if (isAdmin) AppDestination.AdminHome
                    else AppDestination.UserHome
            },

            onRegister = {
                destination = AppDestination.Register
            }
        )

        // REGISTER
        AppDestination.Register -> RegisterScreen(
            onRegister = {
                destination = AppDestination.Login
            },
            onLogin = {
                destination = AppDestination.Login
            }
        )

        // HOME
        AppDestination.UserHome -> UserHomeScreen(
            activeName,
            userNav,
            navigate
        )

        AppDestination.AdminHome -> AdminHomeScreen(
            activeName,
            adminNav,
            navigate
        )

        // PROFILE
        AppDestination.Profile -> ProfileScreen(
            email = activeEmail,
            onBack = {
                destination = homeDestination
            },
            onEditProfile = {
                destination = AppDestination.EditProfile
            },
            onLogout = {
                activeEmail = ""
                activeName = ""
                isAdmin = false
                destination = AppDestination.Login
            }
        )

        AppDestination.EditProfile -> EditProfileScreen(
            email = activeEmail,
            onBack = {
                destination = AppDestination.Profile
            },
            onSave = {
                destination = AppDestination.Profile
            }
        )

        // CATALOG
        AppDestination.Catalog -> CatalogListScreen(
            isAdmin = isAdmin,
            navItems = navItems,
            selectedDestination = AppDestination.Catalog,
            onNavigate = navigate,

            onOpenMenu = {
                selectedItem = it
                destination = AppDestination.CatalogDetail
            },

            onAddMenu = {
                editing = false
                destination = AppDestination.CatalogForm
            }
        )

        AppDestination.CatalogDetail -> CatalogDetailScreen(
            menuName = selectedItem,
            isAdmin = isAdmin,

            onBack = {
                destination = AppDestination.Catalog
            },

            onReviews = {
                destination = AppDestination.Reviews
            },

            onEdit = {
                editing = true
                destination = AppDestination.CatalogForm
            },

            onOrder = {
                destination = AppDestination.OrderForm
            }
        )

        AppDestination.CatalogForm -> CatalogFormScreen(
            isEditing = editing,

            onBack = {
                destination =
                    if (editing) AppDestination.CatalogDetail
                    else AppDestination.Catalog
            },

            onSave = {
                destination =
                    if (editing) AppDestination.CatalogDetail
                    else AppDestination.Catalog
            }
        )

        // ORDER LIST
        AppDestination.Orders -> OrderListScreen(
            isAdmin = isAdmin,
            orders = orderingViewModel.orders,
            navItems = navItems,
            selectedDestination = AppDestination.Orders,
            onNavigate = navigate,

            onOpenOrder = {
                selectedItem = it
                destination = AppDestination.OrderDetail
            },

            onCreateOrder = {
                destination = AppDestination.OrderForm
            }
        )

        // ORDER DETAIL
        AppDestination.OrderDetail -> OrderDetailScreen(
            order = orderingViewModel.findOrder(selectedItem),

            onBack = {
                destination = AppDestination.Orders
            },

            onReview = {
                val order =
                    orderingViewModel.findOrder(selectedItem)

                if (order != null) {
                    selectedItem = order.menuName
                    destination = AppDestination.ReviewForm
                }
            }
        )

        // ORDER FORM
        AppDestination.OrderForm -> OrderFormScreen(
            menuName = selectedItem.ifBlank {
                "Nasi ayam sambal matah"
            },

            standName = "Warung Bu Rini",
            unitPrice = 18000,

            onBack = {
                destination = AppDestination.Catalog
            },

            onSubmit = { quantity, fulfillment, notes ->

                val newOrder = orderingViewModel.createOrder(
                    menuName = selectedItem.ifBlank {
                        "Nasi ayam sambal matah"
                    },
                    standName = "Warung Bu Rini",
                    unitPrice = 18000,
                    quantity = quantity,
                    fulfillment = fulfillment,
                    notes = notes,
                    estimatedPickup = "12:30"
                )

                if (newOrder != null) {
                    selectedItem = newOrder.id
                    destination = AppDestination.Orders
                }
            }
        )

        // REVIEWS
        AppDestination.Reviews -> ReviewListScreen(
            menuName = selectedItem,
            navItems = emptyList(),
            selectedDestination = null,
            onNavigate = navigate,

            onBack = {
                destination = AppDestination.CatalogDetail
            },

            onOpenReview = {
                selectedItem = it
                destination = AppDestination.ReviewDetail
            },

            onWriteReview = {
                selectedItem = "Nasi ayam sambal matah"
                destination = AppDestination.ReviewForm
            }
        )

        AppDestination.ReviewDetail -> ReviewDetailScreen(
            dish = selectedItem,

            onBack = {
                destination = AppDestination.Reviews
            },

            onEdit = {
                destination = AppDestination.ReviewForm
            }
        )

        AppDestination.ReviewForm -> ReviewFormScreen(
            dish = selectedItem.ifBlank {
                "Nasi ayam sambal matah"
            },

            onBack = {
                destination = AppDestination.Reviews
            },

            onSave = {
                destination = AppDestination.Reviews
            }
        )

        // FAVORITES
        AppDestination.Favorites -> FavoritesScreen(
            navItems = favoritesNav,
            selectedDestination = AppDestination.Favorites,
            onNavigate = navigate,

            onOpenMenu = {
                selectedItem = it
                destination = AppDestination.CatalogDetail
            },

            onSubscriptions = {
                destination = AppDestination.Subscriptions
            }
        )

        AppDestination.Subscriptions -> SubscriptionScreen(
            onBack = {
                destination = AppDestination.Favorites
            }
        )

        // WALLET
        AppDestination.Wallet -> WalletScreen(
            navItems = walletNav,
            selectedDestination = AppDestination.Wallet,
            onNavigate = navigate,

            onTopUp = {
                destination = AppDestination.TopUp
            }
        )

        AppDestination.TopUp -> TopUpScreen(
            onBack = {
                destination = AppDestination.Wallet
            },

            onSubmit = {
                destination = AppDestination.Wallet
            }
        )

        // STANDS
        AppDestination.Stands -> StandListScreen(
            navItems = adminNav,
            selectedDestination = AppDestination.Stands,
            onNavigate = navigate,

            onOpenStand = {
                selectedItem = it
                destination = AppDestination.StandDetail
            },

            onAddStand = {
                editing = false
                destination = AppDestination.StandForm
            }
        )

        AppDestination.StandDetail -> StandDetailScreen(
            standName = selectedItem,

            onBack = {
                destination = AppDestination.Stands
            },

            onEdit = {
                editing = true
                destination = AppDestination.StandForm
            }
        )

        AppDestination.StandForm -> StandFormScreen(
            isEditing = editing,

            onBack = {
                destination =
                    if (editing) AppDestination.StandDetail
                    else AppDestination.Stands
            },

            onSave = {
                destination =
                    if (editing) AppDestination.StandDetail
                    else AppDestination.Stands
            }
        )
    }
}
