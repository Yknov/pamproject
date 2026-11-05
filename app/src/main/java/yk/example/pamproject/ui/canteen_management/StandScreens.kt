package yk.example.pamproject.ui.canteen_management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.components.ActionRow
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction

private data class DemoStand(val name: String, val category: String, val hours: String)
private val demoStands = listOf(
    DemoStand("Warung Bu Rini", "Makanan rumahan", "Buka · 08.00–15.00"),
    DemoStand("Kedai Segar", "Minuman dan camilan", "Buka · 09.00–16.00"),
)

@Composable
fun StandListScreen(
    navItems: List<CanteenNavItem>,
    selectedDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onOpenStand: (String) -> Unit,
    onAddStand: () -> Unit,
) {
    CanteenScaffold("Manajemen stand", navItems = navItems, selectedDestination = selectedDestination, onNavigate = onNavigate) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { PageIntro("Stand kantin", "Kelola profil dan informasi stand.") }
            item { PrimaryAction("Tambah stand", onAddStand) }
            items(demoStands) { stand ->
                ActionRow(stand.name, "${stand.category} · ${stand.hours}", onClick = { onOpenStand(stand.name) })
            }
            item { InfoPanel("Data contoh", "Daftar stand ditampilkan sebagai pratinjau UI.") }
        }
    }
}

@Composable
fun StandDetailScreen(standName: String, onBack: () -> Unit, onEdit: () -> Unit) {
    val stand = demoStands.firstOrNull { it.name == standName } ?: demoStands.first()
    CanteenScaffold("Detail stand", onBack = onBack) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                    Text("Foto stand", style = MaterialTheme.typography.titleMedium)
                    Text("Gambar stand dapat ditambahkan nanti.", style = MaterialTheme.typography.bodyMedium)
                }
            }
            PageIntro(stand.name, stand.category)
            InfoPanel("Jam operasional", stand.hours)
            InfoPanel("Menu stand", "Menu terkait akan ditampilkan setelah data tersedia.")
            PrimaryAction("Edit informasi stand", onEdit)
        }
    }
}

@Composable
fun StandFormScreen(isEditing: Boolean, onBack: () -> Unit, onSave: () -> Unit) {
    var name by rememberSaveable { mutableStateOf(if (isEditing) "Warung Bu Rini" else "") }
    var category by rememberSaveable { mutableStateOf("") }
    var hours by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    CanteenScaffold(if (isEditing) "Edit stand" else "Tambah stand", onBack = onBack) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            PageIntro("Informasi stand", "Informasi ini membantu pengguna menemukan kantinmu.")
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Nama stand") }, singleLine = true)
            OutlinedTextField(category, { category = it }, Modifier.fillMaxWidth(), label = { Text("Kategori") }, singleLine = true)
            OutlinedTextField(hours, { hours = it }, Modifier.fillMaxWidth(), label = { Text("Jam operasional") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
            OutlinedTextField(description, { description = it }, Modifier.fillMaxWidth(), label = { Text("Deskripsi") }, minLines = 3)
            InfoPanel("Foto stand", "Upload foto akan dihubungkan pada tahap berikutnya.")
            PrimaryAction("Simpan stand", onSave)
        }
    }
}
