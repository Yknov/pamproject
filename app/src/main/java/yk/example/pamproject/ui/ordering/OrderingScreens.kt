package yk.example.pamproject.ui.ordering

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import yk.example.pamproject.ui.components.StatusLabel

private data class DemoOrder(val title: String, val orderId: String, val status: String, val total: String)
private val demoOrders = listOf(
    DemoOrder("Nasi ayam sambal matah", "CK-1024", "Sedang dibuat", "Rp18.000"),
    DemoOrder("Mie ayam jamur", "CK-1018", "Siap diambil", "Rp15.000"),
)

@Composable
fun OrderListScreen(
    isAdmin: Boolean,
    navItems: List<CanteenNavItem>,
    selectedDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onOpenOrder: (String) -> Unit,
    onCreateOrder: () -> Unit,
) {
    CanteenScaffold("Pesanan", navItems = navItems, selectedDestination = selectedDestination, onNavigate = onNavigate) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { PageIntro(if (isAdmin) "Antrean pesanan" else "Pesanan saya", "Contoh tampilan status pemesanan kantin.") }
            if (!isAdmin) item { PrimaryAction("Pesan makanan", onCreateOrder) }
            items(demoOrders) { order ->
                Surface(
                    onClick = { onOpenOrder(order.orderId) },
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 1.dp,
                ) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Pesanan #${order.orderId}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            StatusLabel(order.status)
                        }
                        Text(order.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (isAdmin) "Meja takeaway" else "Hari ini · 11.45", style = MaterialTheme.typography.bodySmall)
                            Text(order.total, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
            item { InfoPanel("Data contoh", "Daftar pesanan akan menggunakan data akun setelah backend terhubung.") }
        }
    }
}

@Composable
fun OrderDetailScreen(orderId: String, onBack: () -> Unit, onReview: () -> Unit) {
    val order = demoOrders.firstOrNull { it.orderId == orderId } ?: demoOrders.first()
    CanteenScaffold("Detail pesanan", onBack = onBack) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PageIntro("Pesanan #${order.orderId}", "Warung Bu Rini · Hari ini, 11.45")
            Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp), tonalElevation = 1.dp) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(order.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("1 × ${order.total}", style = MaterialTheme.typography.bodyMedium)
                    Text("Jenis pesanan · Takeaway", style = MaterialTheme.typography.bodyMedium)
                    Text("Total  ${order.total}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
            Text("Status pesanan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            listOf("Pesanan diterima", "Sedang dibuat", "Siap diambil").forEachIndexed { index, step ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(if (index < 2) "●" else "○", color = if (index < 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                    Column {
                        Text(step, style = MaterialTheme.typography.bodyLarge)
                        if (index == 1) Text("Status saat ini", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            PrimaryAction("Beri ulasan", onReview)
        }
    }
}

@Composable
fun OrderFormScreen(onBack: () -> Unit, onSubmit: () -> Unit) {
    var quantity by rememberSaveable { mutableStateOf("1") }
    var notes by rememberSaveable { mutableStateOf("") }
    var fulfillment by rememberSaveable { mutableStateOf("Takeaway") }
    CanteenScaffold("Buat pesanan", onBack = onBack) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PageIntro("Konfirmasi pesanan", "Periksa pilihan sebelum dikirim ke stand.")
            Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp), tonalElevation = 1.dp) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Nasi ayam sambal matah", style = MaterialTheme.typography.titleMedium)
                    Text("Warung Bu Rini", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Rp18.000", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
            OutlinedTextField(quantity, { quantity = it }, Modifier.fillMaxWidth(), label = { Text("Jumlah porsi") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Text("Cara menikmati", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Takeaway", "Dine-in").forEach { option ->
                    FilterChip(selected = fulfillment == option, onClick = { fulfillment = option }, label = { Text(option) })
                }
            }
            OutlinedTextField(notes, { notes = it }, Modifier.fillMaxWidth(), label = { Text("Catatan untuk stand") }, minLines = 2)
            InfoPanel("Pembayaran", "Saldo CanteenPay akan tersedia setelah integrasi pembayaran.")
            Spacer(Modifier.weight(1f))
            PrimaryAction("Konfirmasi pesanan", onSubmit)
        }
    }
}
