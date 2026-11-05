package yk.example.pamproject.ui.home.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserHomeScreen(name: String, onProfile: () -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("CanteenKu", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text("Halo, $name", style = MaterialTheme.typography.bodyMedium)
            }
            TextButton(onClick = onProfile) { Text("Profil") }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Mau makan apa?", style = MaterialTheme.typography.headlineMedium)
            Text("Temukan makanan dan minuman dari stand kantin kampus.", style = MaterialTheme.typography.bodyLarge)
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp)) {
            Text("Jelajahi menu")
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Pesanan aktif", style = MaterialTheme.typography.titleLarge)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Belum ada pesanan", style = MaterialTheme.typography.titleMedium)
                    Text("Pesanan yang dibuat akan muncul di sini.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Lainnya", style = MaterialTheme.typography.titleLarge)
            UserDestination("Favorit dan ulasan", "Simpan menu dan bagikan pengalamanmu.")
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            UserDestination("CanteenPay", "Saldo dan transaksi pembayaran kantin.")
        }
        Spacer(Modifier.height(4.dp))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Keluar") }
    }
}

@Composable
private fun UserDestination(title: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
