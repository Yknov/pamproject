package yk.example.pamproject.ui.catalog

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.components.ActionRow
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction
import yk.example.pamproject.ui.components.StatusLabel
import yk.example.pamproject.ui.reviews.reviewsForMenu

data class DemoMenu(val name: String, val stand: String, val price: String, val category: String)

val demoMenus = listOf(
    DemoMenu("Nasi ayam sambal matah", "Warung Bu Rini", "Rp18.000", "Makanan"),
    DemoMenu("Mie ayam jamur", "Mie Pak Darto", "Rp15.000", "Makanan"),
    DemoMenu("Es teh kampus", "Kedai Segar", "Rp5.000", "Minuman"),
)

@Composable
fun CatalogListScreen(

    isAdmin: Boolean,
    navItems: List<CanteenNavItem>,
    selectedDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onOpenMenu: (String) -> Unit,
    onAddMenu: () -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("Semua") }
    val filtered = demoMenus.filter {
        (category == "Semua" || it.category == category) &&
            (query.isBlank() || it.name.contains(query, ignoreCase = true) || it.stand.contains(query, ignoreCase = true))
    }
    CanteenScaffold(
        title = "Katalog menu",
        navItems = navItems,
        selectedDestination = selectedDestination,
        onNavigate = onNavigate,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { PageIntro("Menu kantin", "Pilihan makanan dan minuman dari berbagai stand.") }
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    label = { Text("Cari menu atau stand") },
                    singleLine = true,
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Semua", "Makanan", "Minuman").forEach { filter ->
                        FilterChip(
                            selected = category == filter,
                            onClick = { category = filter },
                            label = { Text(filter) },
                            shape = RoundedCornerShape(50),
                        )
                    }
                }
            }
            if (isAdmin) item { PrimaryAction("Tambah menu", onAddMenu) }
            item {
                Text(
                    "Daftar menu (${filtered.size})",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            items(filtered) { menu -> MenuListRow(menu = menu, onClick = { onOpenMenu(menu.name) }) }
            if (filtered.isEmpty()) item { InfoPanel("Menu tidak ditemukan", "Coba kata kunci atau kategori yang berbeda.") }
        }
    }
}

@Composable
private fun MenuListRow(menu: DemoMenu, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FoodImagePlaceholder(menu.category)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(menu.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${menu.stand} · ${menu.category}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(menu.price, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Text("›", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun FoodImagePlaceholder(category: String) {
    Surface(
        modifier = Modifier.size(76.dp),
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
                style = MaterialTheme.typography.titleLarge,
            )
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

@Composable
fun CatalogDetailScreen(
    menuName: String,
    isAdmin: Boolean,
    onBack: () -> Unit,
    onReviews: () -> Unit,
    onEdit: () -> Unit,
) {
    val menu = demoMenus.firstOrNull { it.name == menuName } ?: demoMenus.first()
    CanteenScaffold(title = "Detail menu", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(190.dp),
                color = if (menu.category == "Minuman") {
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                shape = RoundedCornerShape(18.dp),
            ) {
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
            PageIntro(menu.name.replaceFirstChar { it.uppercase() }, "${menu.stand} · ${menu.category}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(menu.price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                StatusLabel(menu.category)
            }
            Text(
                "Menu pilihan kantin kampus. Informasi bahan dan deskripsi dapat dilengkapi oleh pengelola stand.",
                style = MaterialTheme.typography.bodyMedium,
            )
            val reviews = reviewsForMenu(menu.name)
            val averageRating = reviews.map { it.stars }.average()
            Text("Rating & ulasan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            if (reviews.isNotEmpty()) {
                Text(
                    "★ ${"%.1f".format(averageRating)} · ${reviews.size} ulasan contoh",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Text(reviews.first().comment, style = MaterialTheme.typography.bodyMedium)
            } else {
                Text(
                    "Belum ada ulasan untuk menu ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            ActionRow("Lihat semua ulasan", "Ulasan untuk ${menu.name}", onClick = onReviews)
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
fun CatalogFormScreen(isEditing: Boolean, onBack: () -> Unit, onSave: () -> Unit) {
    var name by rememberSaveable { mutableStateOf(if (isEditing) "Nasi ayam sambal matah" else "") }
    var price by rememberSaveable { mutableStateOf(if (isEditing) "18000" else "") }
    var description by rememberSaveable { mutableStateOf("") }
    CanteenScaffold(title = if (isEditing) "Edit menu" else "Tambah menu", onBack = onBack) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            PageIntro(if (isEditing) "Perbarui menu" else "Menu baru", "Lengkapi informasi menu untuk katalog kantin.")
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Nama menu") }, singleLine = true)
            OutlinedTextField(
                price,
                { price = it },
                Modifier.fillMaxWidth(),
                label = { Text("Harga") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            OutlinedTextField(description, { description = it }, Modifier.fillMaxWidth(), label = { Text("Deskripsi") }, minLines = 3)
            InfoPanel("Foto menu", "Pemilihan dan upload gambar akan dihubungkan pada tahap berikutnya.")
            Spacer(Modifier.weight(1f))
            PrimaryAction("Simpan menu", onSave)
        }
    }
}
