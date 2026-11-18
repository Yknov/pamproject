package yk.example.pamproject.ui.session

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import yk.example.pamproject.domain.auth.model.UserSession

class SessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    fun onLoginSuccess(session: UserSession) {
        _uiState.update { it.copy(session = session) }
    }

    fun onLogout() {
        _uiState.value = SessionUiState()
    }

    fun selectItem(name: String) {
        _uiState.update { it.copy(selectedItem = name) }
    }

    fun clearSelectedItem() {
        _uiState.update { it.copy(selectedItem = "") }
    }

    fun setCatalogEditing(editing: Boolean) {
        _uiState.update { it.copy(isEditingCatalog = editing) }
    }
}
