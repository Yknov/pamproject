package yk.example.pamproject.ui.home.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
fun AdminHomeScreen(
    name: String,
    navItems: List<CanteenNavItem>,
    onNavigate: (AppDestination) -> Unit,
) {
    CanteenScaffold(
        title = "Dashboard",
        navItems = navItems,
        selectedDestination = AppDestination.AdminHome,
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
                    description = "Ringkasan katalog menu dan ulasan kantin kampus.",
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AdminSummaryCard(
                        value = "${demoMenus.size} Menu",
                        label = "Terdaftar di katalog",
                        modifier = Modifier.weight(1f),
                    )
                    AdminSummaryCard(
                        value = "${demoReviews.size} Ulasan",
                        label = "Masukan pengguna",
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item {
                PrimaryAction("Kelola katalog menu", { onNavigate(AppDestination.Catalog) })
            }

            item {
                SectionHeading("Kelola kantin")
            }

            item {
                ActionRow(
                    title = "Menu makanan & minuman",
                    subtitle = "Tambah, perbarui, dan atur informasi katalog menu",
                    onClick = { onNavigate(AppDestination.Catalog) },
                )
            }

            item {
                ActionRow(
                    title = "Ulasan & rating menu",
                    subtitle = "Pantau penilaian dan komentar dari pengguna",
                    onClick = { onNavigate(AppDestination.Reviews) },
                )
            }

            item {
                ActionRow(
                    title = "Profil admin",
                    subtitle = "Kelola informasi akun pengelola",
                    onClick = { onNavigate(AppDestination.Profile) },
                )
            }

            item {
                InfoPanel(
                    title = "Mode pratinjau",
                    message = "Angka operasional akan terhubung secara dinamis setelah integrasi data selesai.",
                )
            }
        }
    }
}

@Composable
private fun AdminSummaryCard(
    value: String,
    label: String,
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
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
