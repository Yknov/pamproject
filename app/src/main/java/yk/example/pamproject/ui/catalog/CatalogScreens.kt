package yk.example.pamproject.ui.catalog

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yk.example.pamproject.domain.catalog.model.Menu
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.catalog.detail.CatalogDetailUiState
import yk.example.pamproject.ui.catalog.form.CatalogFormUiState
import yk.example.pamproject.ui.catalog.list.CatalogListUiState
import yk.example.pamproject.ui.components.ActionRow
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction
import yk.example.pamproject.ui.components.StatusLabel

private const val MAX_IMAGE_SIZE_BYTES = 5L * 1024 * 1024

@Composable
fun CatalogListScreen(
    uiState: CatalogListUiState,
    isAdmin: Boolean,
    navItems: List<CanteenNavItem>,
    selectedDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onOpenMenu: (String) -> Unit,
    onAddMenu: () -> Unit,
) {
    CanteenScaffold(
        title = "Katalog menu",
        navItems = navItems,
        selectedDestination = selectedDestination,
        onNavigate = onNavigate,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { PageIntro("Menu kantin", "Pilihan makanan dan minuman dari berbagai stand.") }
            item {
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    label = { Text("Cari menu atau stand") },
                    singleLine = true,
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.categories.forEach { filter ->
                        FilterChip(
                            selected = uiState.selectedCategory == filter,
                            onClick = { onCategoryChange(filter) },
                            label = { Text(filter) },
                            shape = RoundedCornerShape(50),
                        )
                    }
                }
            }
            if (isAdmin) item { PrimaryAction("Tambah menu", onAddMenu) }
            item {
                Text(
                    "Daftar menu (${uiState.menus.size})",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            items(uiState.menus) { menu ->
                MenuListRow(menu = menu, onClick = { onOpenMenu(menu.name) })
            }
            if (uiState.errorMessage != null) {
                item { InfoPanel("Gagal memuat menu", uiState.errorMessage) }
            } else if (uiState.menus.isEmpty() && !uiState.isLoading) {
                item {
                    InfoPanel(
                        "Menu tidak ditemukan",
                        "Coba kata kunci atau kategori yang berbeda.",
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuListRow(menu: Menu, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MenuImageThumbnail(
                imageUri = menu.imageUri,
                category = menu.category,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(menu.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    "${menu.stand} · ${menu.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    menu.price,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Text("›", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun FoodImagePlaceholder(
    category: String,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
    emojiStyleLarge: Boolean = false,
) {
    Surface(
        modifier = modifier,
        color = if (category == "Minuman") {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        },
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = if (category == "Minuman") "🥤" else "🍲",
                style = if (emojiStyleLarge) {
                    MaterialTheme.typography.headlineLarge
                } else {
                    MaterialTheme.typography.titleLarge
                },
            )
            if (showLabel) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (category == "Minuman") {
                        MaterialTheme.colorScheme.onTertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    },
                )
            }
        }
    }
}

@Composable
private fun MenuImageThumbnail(
    imageUri: String?,
    category: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val bitmap = rememberBitmapFromUri(imageUri)
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = "Foto menu",
            modifier = modifier,
            contentScale = contentScale,
        )
    } else {
        FoodImagePlaceholder(category = category, modifier = modifier)
    }
}

@Composable
private fun rememberBitmapFromUri(uriString: String?): ImageBitmap? {
    val context = LocalContext.current
    val state = produceState<ImageBitmap?>(initialValue = null, key1 = uriString) {
        value = if (uriString.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(Uri.parse(uriString))?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                    }
                }.getOrNull()
            }
        }
    }
    return state.value
}

private fun getFileSizeBytes(context: Context, uri: Uri): Long? {
    context.contentResolver.openAssetFileDescriptor(uri, "r")?.use { descriptor ->
        if (descriptor.length >= 0) return descriptor.length
    }
    context.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
        val index = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (index != -1 && cursor.moveToFirst() && !cursor.isNull(index)) {
            return cursor.getLong(index)
        }
    }
    return null
}

@Composable
fun CatalogDetailScreen(
    uiState: CatalogDetailUiState,
    isAdmin: Boolean,
    onBack: () -> Unit,
    onReviews: () -> Unit,
    onEdit: () -> Unit,
) {
    val menu = uiState.menu
    CanteenScaffold(title = "Detail menu", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (menu == null) {
                if (uiState.errorMessage != null) {
                    InfoPanel("Gagal memuat menu", uiState.errorMessage)
                } else {
                    PageIntro("Memuat menu", "Mohon tunggu sebentar.")
                }
                return@Column
            }
            val heroBitmap = rememberBitmapFromUri(menu.imageUri)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                color = if (menu.category == "Minuman") {
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                shape = RoundedCornerShape(18.dp),
            ) {
                if (heroBitmap != null) {
                    Image(
                        bitmap = heroBitmap,
                        contentDescription = "Foto ${menu.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(
                            text = if (menu.category == "Minuman") "🥤" else "🍲",
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Foto menu",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            "Gambar akan ditampilkan di sini",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        )
                    }
                }
            }
            PageIntro(menu.name.replaceFirstChar { it.uppercase() }, "${menu.stand} · ${menu.category}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    menu.price,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                StatusLabel(menu.category)
            }
            Text(
                menu.description.ifBlank {
                    "Menu pilihan kantin kampus. Informasi bahan dan deskripsi dapat dilengkapi oleh pengelola stand."
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            Text("Rating & ulasan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            if (uiState.reviews.isNotEmpty()) {
                Text(
                    "★ ${String.format(Locale.US, "%.1f", uiState.averageRating)} · ${uiState.reviews.size} ulasan contoh",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Text(uiState.reviews.first().comment, style = MaterialTheme.typography.bodyMedium)
            } else {
                Text(
                    "Belum ada ulasan untuk menu ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            ActionRow("Lihat semua ulasan", "Ulasan untuk ${menu.name}", onClick = onReviews)
            if (uiState.errorMessage != null) {
                InfoPanel("Gagal memuat data", uiState.errorMessage)
            }
            Spacer(Modifier.weight(1f))
            if (isAdmin) {
                PrimaryAction("Edit menu", onEdit)
            } else {
                PrimaryAction("Lihat & tulis ulasan", onReviews)
            }
        }
    }
}

@Composable
fun CatalogFormScreen(
    uiState: CatalogFormUiState,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onImageSelected: (String) -> Unit,
    onImageCleared: () -> Unit,
    onImageError: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val size = getFileSizeBytes(context, uri)
        when {
            size == null -> onImageError("Gagal membaca ukuran gambar. Coba pilih file lain.")
            size > MAX_IMAGE_SIZE_BYTES -> onImageError("Ukuran gambar maksimal 5 MB.")
            else -> onImageSelected(uri.toString())
        }
    }

    CanteenScaffold(
        title = if (uiState.isEditing) "Edit menu" else "Tambah menu",
        onBack = onBack,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            PageIntro(
                if (uiState.isEditing) "Perbarui menu" else "Menu baru",
                "Lengkapi informasi menu untuk katalog kantin.",
            )
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nama menu") },
                singleLine = true,
            )
            OutlinedTextField(
                value = uiState.price,
                onValueChange = onPriceChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Harga") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Deskripsi") },
                minLines = 3,
            )

            Text(
                "Foto menu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                "Format gambar, maks. 5 MB.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            val selectedBitmap = rememberBitmapFromUri(uiState.imageUri)
            if (uiState.imageUri != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    if (selectedBitmap != null) {
                        Image(
                            bitmap = selectedBitmap,
                            contentDescription = "Pratinjau foto menu",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text("Memuat pratinjau gambar...")
                        }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text(if (uiState.imageUri == null) "Pilih gambar" else "Ganti gambar")
                }
                if (uiState.imageUri != null) {
                    OutlinedButton(onClick = onImageCleared) {
                        Text("Hapus")
                    }
                }
            }
            if (uiState.imageError != null) {
                InfoPanel("Foto menu", uiState.imageError)
            }

            if (uiState.errorMessage != null) {
                InfoPanel("Gagal menyimpan", uiState.errorMessage)
            }
            Spacer(Modifier.height(8.dp))
            PrimaryAction(if (uiState.isSaving) "Menyimpan..." else "Simpan menu", onSave)
        }
    }
}
