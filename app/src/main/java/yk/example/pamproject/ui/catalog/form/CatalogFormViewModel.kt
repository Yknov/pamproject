package yk.example.pamproject.ui.catalog.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yk.example.pamproject.data.catalog.DummyCatalogRepository
import yk.example.pamproject.domain.catalog.model.Menu
import yk.example.pamproject.domain.catalog.repository.CatalogRepository

class CatalogFormViewModel(
    private val catalogRepository: CatalogRepository,
) : ViewModel() {

    constructor() : this(DummyCatalogRepository())

    private val _uiState = MutableStateFlow(CatalogFormUiState())
    val uiState: StateFlow<CatalogFormUiState> = _uiState.asStateFlow()

    private val _effect = Channel<CatalogFormEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var initializedKey: String? = null

    fun initialize(isEditing: Boolean, menuName: String) {
        val key = "$isEditing|$menuName"
        if (initializedKey == key) return
        initializedKey = key
        if (!isEditing) {
            _uiState.value = CatalogFormUiState(isEditing = false)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isEditing = true, errorMessage = null, imageError = null) }
            catalogRepository.getMenuByName(menuName)
                .onSuccess { menu ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditing = true,
                            menuId = menu?.id,
                            name = menu?.name.orEmpty(),
                            price = menu?.price.orEmpty(),
                            description = menu?.description.orEmpty(),
                            category = menu?.category ?: "Makanan",
                            stand = menu?.stand.orEmpty(),
                            imageUri = menu?.imageUri,
                            imageError = null,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal memuat menu.",
                        )
                    }
                }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onPriceChange(value: String) {
        _uiState.update { it.copy(price = value, errorMessage = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value, errorMessage = null) }
    }

    fun onImageSelected(uri: String) {
        _uiState.update { it.copy(imageUri = uri, imageError = null) }
    }

    fun onImageCleared() {
        _uiState.update { it.copy(imageUri = null, imageError = null) }
    }

    fun onImageError(message: String) {
        _uiState.update { it.copy(imageError = message) }
    }

    fun onSaveClick() {
        val state = _uiState.value
        if (!state.canSave) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            catalogRepository.saveMenu(
                Menu(
                    id = state.menuId ?: "",
                    name = state.name.trim(),
                    stand = state.stand.ifBlank { "Stand" },
                    price = state.price.trim(),
                    category = state.category,
                    description = state.description.trim(),
                    imageUri = state.imageUri,
                ),
            ).onSuccess {
                _uiState.update { it.copy(isSaving = false) }
                _effect.send(CatalogFormEffect.Saved)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Gagal menyimpan menu.",
                    )
                }
            }
        }
    }
}

sealed interface CatalogFormEffect {
    data object Saved : CatalogFormEffect
}
