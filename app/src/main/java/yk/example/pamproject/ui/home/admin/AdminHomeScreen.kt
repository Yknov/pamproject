package yk.example.pamproject.ui.home.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.components.ActionRow
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction
import yk.example.pamproject.ui.components.SectionHeading

@Composable
fun AdminHomeScreen(name: String, navItems: List<CanteenNavItem>, onNavigate: (AppDestination) -> Unit) {
    CanteenScaffold(
        title = "Dashboard",
        navItems = navItems,
        selectedDestination = AppDestination.AdminHome,
        onNavigate = onNavigate,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { PageIntro("Halo, ${name.substringBefore('@')}", "Ringkasan operasional kantin.") }
            item { SectionHeading("Pesanan masuk") }
            item { InfoPanel("Antrean siap dikelola", "Pesanan baru dan status pembuatan akan muncul pada halaman antrean.") }
            item { PrimaryAction("Buka antrean pesanan", { onNavigate(AppDestination.Orders) }) }
            item { SectionHeading("Kelola kantin") }
            item { ActionRow("Menu makanan", "Atur katalog dan ketersediaan menu", onClick = { onNavigate(AppDestination.Catalog) }) }
            item { ActionRow("Stand kantin", "Profil, kategori, dan jam operasional", onClick = { onNavigate(AppDestination.Stands) }) }
            item { ActionRow("Ulasan menu", "Lihat rating dari halaman detail menu", onClick = { onNavigate(AppDestination.Catalog) }) }
            item { Text("Angka operasional akan tampil setelah data terhubung.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}
