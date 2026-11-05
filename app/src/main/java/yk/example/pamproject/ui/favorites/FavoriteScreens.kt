package yk.example.pamproject.ui.favorites

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
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.SectionHeading
import yk.example.pamproject.ui.components.ActionRow

@Composable
fun FavoritesScreen(
    navItems: List<CanteenNavItem>,
    selectedDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onOpenMenu: (String) -> Unit,
    onSubscriptions: () -> Unit,
) {
    CanteenScaffold("Favorit", navItems = navItems, selectedDestination = selectedDestination, onNavigate = onNavigate) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { PageIntro("Menu tersimpan", "Akses cepat ke menu pilihanmu.") }
            item { ActionRow("Langganan stand", "Lihat kabar stand yang diikuti", onClick = onSubscriptions) }
            item { SectionHeading("Favorit saya") }
            items(demoMenus.take(2)) { menu ->
                ActionRow(menu.name, "${menu.stand} · ${menu.price}", onClick = { onOpenMenu(menu.name) })
            }
            item { InfoPanel("Data contoh", "Daftar favorit akan tersimpan pada akun setelah backend tersedia.") }
        }
    }
}

@Composable
fun SubscriptionScreen(onBack: () -> Unit) {
    CanteenScaffold("Langganan stand", onBack = onBack) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { PageIntro("Stand yang diikuti", "Dapatkan informasi menu dari stand favorit.") }
            item { ActionRow("Warung Bu Rini", "Menu rumahan dan lauk harian", trailing = "Mengikuti", onClick = {}) }
            item { ActionRow("Kedai Segar", "Minuman dan camilan", trailing = "Mengikuti", onClick = {}) }
            item { InfoPanel("Langganan contoh", "Pengelolaan langganan akan terhubung dengan data akun nanti.") }
        }
    }
}
