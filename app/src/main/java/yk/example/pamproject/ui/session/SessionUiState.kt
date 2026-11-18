package yk.example.pamproject.ui.session

import yk.example.pamproject.domain.auth.model.UserSession
import yk.example.pamproject.navigation.AppDestination

data class SessionUiState(
    val session: UserSession? = null,
    val selectedItem: String = "",
    val isEditingCatalog: Boolean = false,
) {
    val isLoggedIn: Boolean get() = session != null
    val activeEmail: String get() = session?.email.orEmpty()
    val activeName: String get() = session?.name.orEmpty()
    val isAdmin: Boolean get() = session?.isAdmin == true
    val homeDestination: AppDestination
        get() = if (isAdmin) AppDestination.AdminHome else AppDestination.UserHome
}
