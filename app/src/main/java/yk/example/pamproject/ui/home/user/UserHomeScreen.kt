package yk.example.pamproject.ui.home.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import yk.example.pamproject.ui.reviews.demoReviews

@Composable
fun UserHomeScreen(
    name: String,
    navItems: List<CanteenNavItem>,
    onNavigate: (AppDestination) -> Unit,
    onOpenMenu: (String) -> Unit = { onNavigate(AppDestination.CatalogDetail) },
) {
    CanteenScaffold(
        title = "Beranda",
        navItems = navItems,
        selectedDestination = AppDestination.UserHome,
        onNavigate = onNavigate,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                PageIntro(
                    title = "Halo, ${name.substringBefore('@')}",
                    description = "Jelajahi katalog menu kantin kampus dan lihat ulasan mahasiswa.",
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    HomeStatCard(
                        title = "${demoMenus.size} Menu",
                        subtitle = "Tersedia di katalog",
                        modifier = Modifier.weight(1f),
                    )
                    HomeStatCard(
                        title = "★ 4.8 / 5",
                        subtitle = "${demoReviews.size} ulasan mahasiswa",
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item {
                PrimaryAction("Jelajahi semua menu", { onNavigate(AppDestination.Catalog) })
            }

            item {
                SectionHeading("Pilihan kantin", "Katalog", { onNavigate(AppDestination.Catalog) })
            }

            items(demoMenus) { menu ->
                ActionRow(
                    title = menu.name,
                    subtitle = "${menu.stand} · ${menu.category}",
                    trailing = menu.price,
                    onClick = { onOpenMenu(menu.name) },
                )
            }

            item {
                SectionHeading("Ulasan & akun")
            }

            item {
                ActionRow(
                    title = "Rating & ulasan makanan",
                    subtitle = "Lihat pengalaman dan penilaian menu kantin",
                    onClick = { onNavigate(AppDestination.Reviews) },
                )
            }

            item {
                ActionRow(
                    title = "Profil pengguna",
                    subtitle = "Kelola informasi akun CanteenKu",
                    onClick = { onNavigate(AppDestination.Profile) },
                )
            }

            item {
                InfoPanel(
                    title = "Pratinjau CanteenKu",
                    message = "Konten menu dan ulasan di layar ini merupakan contoh tampilan aplikasi.",
                )
            }
        }
    }
}

@Composable
private fun HomeStatCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
