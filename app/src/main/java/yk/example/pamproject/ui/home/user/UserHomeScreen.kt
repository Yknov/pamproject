package yk.example.pamproject.ui.home.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.catalog.demoMenus
import yk.example.pamproject.ui.components.ActionRow
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction
import yk.example.pamproject.ui.components.SectionHeading

@Composable
fun UserHomeScreen(
    name: String,
    navItems: List<CanteenNavItem>,
    onNavigate: (AppDestination) -> Unit,
) {
    CanteenScaffold(
        title = "Beranda",
        navItems = navItems,
        selectedDestination = AppDestination.UserHome,
        onNavigate = onNavigate,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                PageIntro("Halo, ${name.substringBefore('@')}", "Pesan makanan kampus tanpa antre.")
            }
            item { PrimaryAction("Jelajahi semua menu", { onNavigate(AppDestination.Catalog) }) }
            item { SectionHeading("Pesanan aktif", "Lihat semua", { onNavigate(AppDestination.Orders) }) }
            item { InfoPanel("Belum ada pesanan aktif", "Pesanan yang sedang dibuat akan muncul di sini.") }
            item { SectionHeading("Pilihan kantin", "Katalog", { onNavigate(AppDestination.Catalog) }) }
            items(demoMenus.take(2)) { menu ->
                ActionRow(menu.name, "${menu.stand} · ${menu.price}", onClick = { onNavigate(AppDestination.CatalogDetail) })
            }
            item { SectionHeading("Akun dan saldo") }
            item { ActionRow("CanteenPay", "Periksa saldo dan riwayat transaksi", onClick = { onNavigate(AppDestination.Wallet) }) }
            item { ActionRow("Favorit dan langganan", "Menu tersimpan dan stand yang diikuti", onClick = { onNavigate(AppDestination.Favorites) }) }
            item { Text("Konten menu di layar ini adalah contoh tampilan.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}
