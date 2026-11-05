package yk.example.pamproject.ui.home.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun AdminHomeScreen(name: String, onProfile: () -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("CanteenKu · Admin", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text("Halo, $name", style = MaterialTheme.typography.bodyMedium)
            }
            TextButton(onClick = onProfile) { Text("Profil") }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Operasional kantin", style = MaterialTheme.typography.headlineMedium)
            Text("Kelola stand, menu, dan pesanan dari satu tempat.", style = MaterialTheme.typography.bodyLarge)
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Pesanan masuk", style = MaterialTheme.typography.titleLarge)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Antrean pesanan", style = MaterialTheme.typography.titleMedium)
                    Text("Pesanan baru akan tampil di bagian ini.", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Buka antrean") }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Kelola kantin", style = MaterialTheme.typography.titleLarge)
            AdminDestination("Stand kantin", "Informasi dan profil stand")
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            AdminDestination("Menu makanan", "Daftar menu dan ketersediaan")
        }
        Spacer(Modifier.height(4.dp))
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Keluar") }
    }
}

@Composable
private fun AdminDestination(title: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
