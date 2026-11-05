package yk.example.pamproject.ui.canteen_pay

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction

private data class DemoTransaction(val title: String, val date: String, val amount: String)
private val demoTransactions = listOf(
    DemoTransaction("Top-up saldo", "Hari ini · 09.20", "+Rp50.000"),
    DemoTransaction("Nasi ayam sambal matah", "Kemarin · 12.15", "−Rp18.000"),
)

@Composable
fun WalletScreen(navItems: List<CanteenNavItem>, selectedDestination: AppDestination, onNavigate: (AppDestination) -> Unit, onTopUp: () -> Unit) {
    CanteenScaffold("CanteenPay", navItems = navItems, selectedDestination = selectedDestination, onNavigate = onNavigate) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Saldo tersedia · contoh", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .85f))
                        Text("Rp125.000", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold)
                        Text("CanteenPay", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
            item { PrimaryAction("Top-up saldo", onTopUp) }
            item { PageIntro("Riwayat transaksi", "Aktivitas pembayaran dan pengisian saldo.") }
            items(demoTransactions) { transaction ->
                Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(14.dp), tonalElevation = 1.dp) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(transaction.title, style = MaterialTheme.typography.titleSmall)
                            Text(transaction.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(transaction.amount, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            item { InfoPanel("Pratinjau", "Saldo dan transaksi akan berasal dari akun setelah pembayaran terintegrasi.") }
        }
    }
}

@Composable
fun TopUpScreen(onBack: () -> Unit, onSubmit: () -> Unit) {
    var amount by rememberSaveable { mutableStateOf("") }
    var method by rememberSaveable { mutableStateOf("Transfer bank") }
    CanteenScaffold("Top-up saldo", onBack = onBack) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PageIntro("Isi saldo CanteenPay", "Pilih nominal dan metode top-up.")
            OutlinedTextField(amount, { amount = it }, Modifier.fillMaxWidth(), label = { Text("Nominal top-up") }, prefix = { Text("Rp ") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Text("Nominal cepat", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("25.000", "50.000", "100.000").forEach { nominal ->
                    FilterChip(selected = amount == nominal, onClick = { amount = nominal }, label = { Text("Rp$nominal") })
                }
            }
            Text("Metode pembayaran", style = MaterialTheme.typography.titleSmall)
            listOf("Transfer bank", "QRIS").forEach { option ->
                FilterChip(selected = method == option, onClick = { method = option }, label = { Text(option) })
            }
            InfoPanel("Pembayaran belum aktif", "Tampilan ini belum mengirim transaksi atau memotong saldo.")
            PrimaryAction("Lanjutkan", onSubmit)
        }
    }
}
