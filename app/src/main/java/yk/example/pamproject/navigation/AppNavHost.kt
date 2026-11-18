package yk.example.pamproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import yk.example.pamproject.ui.auth.LoginEffect
import yk.example.pamproject.ui.auth.LoginScreen
import yk.example.pamproject.ui.auth.LoginViewModel
import yk.example.pamproject.ui.auth.RegisterEffect
import yk.example.pamproject.ui.auth.RegisterScreen
import yk.example.pamproject.ui.auth.RegisterViewModel
import yk.example.pamproject.ui.catalog.CatalogDetailScreen
import yk.example.pamproject.ui.catalog.CatalogFormScreen
import yk.example.pamproject.ui.catalog.CatalogListScreen
import yk.example.pamproject.ui.catalog.detail.CatalogDetailViewModel
import yk.example.pamproject.ui.catalog.form.CatalogFormEffect
import yk.example.pamproject.ui.catalog.form.CatalogFormViewModel
import yk.example.pamproject.ui.catalog.list.CatalogListViewModel
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.home.admin.AdminHomeScreen
import yk.example.pamproject.ui.home.admin.AdminHomeViewModel
import yk.example.pamproject.ui.home.user.UserHomeScreen
import yk.example.pamproject.ui.home.user.UserHomeViewModel
import yk.example.pamproject.ui.profile.EditProfileEffect
import yk.example.pamproject.ui.profile.EditProfileScreen
import yk.example.pamproject.ui.profile.EditProfileViewModel
import yk.example.pamproject.ui.profile.ProfileScreen
import yk.example.pamproject.ui.profile.ProfileViewModel
import yk.example.pamproject.ui.reviews.ReviewDetailScreen
import yk.example.pamproject.ui.reviews.ReviewFormScreen
import yk.example.pamproject.ui.reviews.ReviewListScreen
import yk.example.pamproject.ui.reviews.detail.ReviewDetailViewModel
import yk.example.pamproject.ui.reviews.form.ReviewFormEffect
import yk.example.pamproject.ui.reviews.form.ReviewFormViewModel
import yk.example.pamproject.ui.reviews.list.ReviewListViewModel
import yk.example.pamproject.ui.session.SessionViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val sessionViewModel: SessionViewModel = viewModel()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()

    val homeDestination = sessionState.homeDestination
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
    val navItems = if (sessionState.isAdmin) adminNav else userNav
    val navigate: (AppDestination) -> Unit = { target ->
        if (target == AppDestination.Reviews) {
            sessionViewModel.clearSelectedItem()
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
            val viewModel: LoginViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is LoginEffect.NavigateToHome -> {
                            sessionViewModel.onLoginSuccess(effect.session)
                            val target = if (effect.session.isAdmin) {
                                AppDestination.AdminHome
                            } else {
                                AppDestination.UserHome
                            }
                            navController.navigate(target.name) {
                                popUpTo(AppDestination.Login.name) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        LoginEffect.NavigateToRegister -> {
                            navController.navigate(AppDestination.Register.name) {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }

            LoginScreen(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
                onLoginClick = viewModel::onLoginClick,
                onRegisterClick = viewModel::onRegisterClick,
            )
        }
        composable(AppDestination.Register.name) {
            val viewModel: RegisterViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        RegisterEffect.NavigateToLogin -> {
                            if (!navController.popBackStack()) {
                                navController.navigate(AppDestination.Login.name) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }

            RegisterScreen(
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
                onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
                onRegisterClick = viewModel::onRegisterClick,
                onLoginClick = viewModel::onLoginClick,
            )
        }
        composable(AppDestination.UserHome.name) {
            val viewModel: UserHomeViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.load()
            }
            LaunchedEffect(sessionState.activeName) {
                viewModel.setDisplayName(sessionState.activeName)
            }

            UserHomeScreen(
                uiState = uiState,
                navItems = userNav,
                onNavigate = navigate,
                onOpenMenu = {
                    sessionViewModel.selectItem(it)
                    navController.navigate(AppDestination.CatalogDetail.name)
                },
            )
        }
        composable(AppDestination.AdminHome.name) {
            val viewModel: AdminHomeViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.load()
            }
            LaunchedEffect(sessionState.activeName) {
                viewModel.setDisplayName(sessionState.activeName)
            }

            AdminHomeScreen(
                uiState = uiState,
                navItems = adminNav,
                onNavigate = navigate,
            )
        }
        composable(AppDestination.Profile.name) {
            val viewModel: ProfileViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(sessionState.activeEmail) {
                viewModel.load(sessionState.activeEmail)
            }

            ProfileScreen(
                uiState = uiState,
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
                    sessionViewModel.onLogout()
                    navController.navigate(AppDestination.Login.name) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(AppDestination.EditProfile.name) {
            val viewModel: EditProfileViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(sessionState.activeEmail) {
                viewModel.load(sessionState.activeEmail)
            }
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        EditProfileEffect.Saved -> {
                            if (!navController.popBackStack()) {
                                navController.navigate(AppDestination.Profile.name)
                            }
                        }
                    }
                }
            }

            EditProfileScreen(
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onPhoneChange = viewModel::onPhoneChange,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Profile.name)
                    }
                },
                onSave = viewModel::onSaveClick,
            )
        }
        composable(AppDestination.Catalog.name) {
            val viewModel: CatalogListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.load()
            }

            CatalogListScreen(
                uiState = uiState,
                isAdmin = sessionState.isAdmin,
                navItems = navItems,
                selectedDestination = AppDestination.Catalog,
                onNavigate = navigate,
                onQueryChange = viewModel::onQueryChange,
                onCategoryChange = viewModel::onCategoryChange,
                onOpenMenu = {
                    sessionViewModel.selectItem(it)
                    navController.navigate(AppDestination.CatalogDetail.name)
                },
                onAddMenu = {
                    sessionViewModel.setCatalogEditing(false)
                    navController.navigate(AppDestination.CatalogForm.name)
                },
            )
        }
        composable(AppDestination.CatalogDetail.name) {
            val viewModel: CatalogDetailViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(sessionState.selectedItem) {
                viewModel.load(sessionState.selectedItem)
            }

            CatalogDetailScreen(
                uiState = uiState,
                isAdmin = sessionState.isAdmin,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Catalog.name)
                    }
                },
                onReviews = {
                    navController.navigate(AppDestination.Reviews.name)
                },
                onEdit = {
                    sessionViewModel.setCatalogEditing(true)
                    navController.navigate(AppDestination.CatalogForm.name)
                },
            )
        }
        composable(AppDestination.CatalogForm.name) {
            val viewModel: CatalogFormViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(sessionState.isEditingCatalog, sessionState.selectedItem) {
                viewModel.initialize(sessionState.isEditingCatalog, sessionState.selectedItem)
            }

            val fallbackDestination = if (sessionState.isEditingCatalog) {
                AppDestination.CatalogDetail
            } else {
                AppDestination.Catalog
            }
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        CatalogFormEffect.Saved -> {
                            if (!navController.popBackStack()) {
                                navController.navigate(fallbackDestination.name)
                            }
                        }
                    }
                }
            }

            CatalogFormScreen(
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onPriceChange = viewModel::onPriceChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onImageSelected = viewModel::onImageSelected,
                onImageCleared = viewModel::onImageCleared,
                onImageError = viewModel::onImageError,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(fallbackDestination.name)
                    }
                },
                onSave = viewModel::onSaveClick,
            )
        }
        composable(AppDestination.Reviews.name) {
            val viewModel: ReviewListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(sessionState.selectedItem) {
                viewModel.load(sessionState.selectedItem)
            }

            ReviewListScreen(
                uiState = uiState,
                navItems = navItems,
                selectedDestination = AppDestination.Reviews,
                onNavigate = navigate,
                onBack = if (sessionState.selectedItem.isNotBlank()) {
                    {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppDestination.CatalogDetail.name)
                        }
                    }
                } else {
                    null
                },
                onOpenReview = {
                    sessionViewModel.selectItem(it)
                    navController.navigate(AppDestination.ReviewDetail.name)
                },
                onWriteReview = {
                    if (sessionState.selectedItem.isBlank()) {
                        sessionViewModel.selectItem("Nasi ayam sambal matah")
                    }
                    navController.navigate(AppDestination.ReviewForm.name)
                },
            )
        }
        composable(AppDestination.ReviewDetail.name) {
            val viewModel: ReviewDetailViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(sessionState.selectedItem) {
                viewModel.load(sessionState.selectedItem)
            }

            ReviewDetailScreen(
                uiState = uiState,
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
            val viewModel: ReviewFormViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(sessionState.selectedItem) {
                viewModel.setDish(sessionState.selectedItem)
            }
            LaunchedEffect(viewModel) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        ReviewFormEffect.Submitted -> {
                            if (!navController.popBackStack()) {
                                navController.navigate(AppDestination.Reviews.name)
                            }
                        }
                    }
                }
            }

            ReviewFormScreen(
                uiState = uiState,
                onRatingChange = viewModel::onRatingChange,
                onCommentChange = viewModel::onCommentChange,
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AppDestination.Reviews.name)
                    }
                },
                onSave = viewModel::onSubmitClick,
            )
        }
    }
}
