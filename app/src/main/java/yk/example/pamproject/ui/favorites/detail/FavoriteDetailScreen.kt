package yk.example.pamproject.ui.favorites.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import yk.example.pamproject.domain.favorites.model.FavoriteMenu
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction

/**
 * Detail screen for a favorite menu featuring a Re-order action and favorite removal action.
 */
@Composable
fun FavoriteDetailScreen(
    favorite: FavoriteMenu?,
    reorderMessage: String?,
    onBack: () -> Unit,
    onQuickReorder: () -> Unit,
    onRemoveFavorite: (String) -> Unit,
) {
    CanteenScaffold(title = "Detail favorit", onBack = onBack) { padding ->
        if (favorite == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                InfoPanel(
                    title = "Menu tidak ditemukan",
                    message = "Menu ini sudah dihapus dari daftar favorit Anda.",
                )
                Spacer(Modifier.weight(1f))
                PrimaryAction("Kembali", onBack)
            }
            return@CanteenScaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PageIntro(
                title = favorite.menuName.replaceFirstChar { it.uppercase() },
                description = "${favorite.standName} · ${favorite.category}",
            )

            Text(
                text = favorite.price,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = favorite.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            PrimaryAction(
                label = "Re-order",
                onClick = onQuickReorder,
            )

            if (reorderMessage != null) {
                InfoPanel(
                    title = "Re-order berhasil",
                    message = reorderMessage,
                )
            }

            OutlinedButton(
                onClick = {
                    onRemoveFavorite(favorite.menuName)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                Text(
                    text = "Hapus dari daftar favorit",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
