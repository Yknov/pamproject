
package yk.example.pamproject.ui.ordering

import androidx.compose.foundation.clickable
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
import yk.example.pamproject.domain.ordering.CustomerOrder
import yk.example.pamproject.domain.ordering.FulfillmentType
import yk.example.pamproject.domain.ordering.OrderStatus
import yk.example.pamproject.navigation.AppDestination
import yk.example.pamproject.ui.components.CanteenNavItem
import yk.example.pamproject.ui.components.CanteenScaffold
import yk.example.pamproject.ui.components.InfoPanel
import yk.example.pamproject.ui.components.PageIntro
import yk.example.pamproject.ui.components.PrimaryAction
import yk.example.pamproject.ui.components.StatusLabel
import java.text.NumberFormat
import java.util.Locale

private fun formatRupiah(amount: Int): String {
    return NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    ).format(amount).replace(",00", "")
}

@Composable
fun OrderListScreen(
    isAdmin: Boolean,
    orders: List<CustomerOrder>,
    navItems: List<CanteenNavItem>,
    selectedDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onOpenOrder: (String) -> Unit,
    onCreateOrder: () -> Unit,
) {
    CanteenScaffold(
        "Pesanan",
        navItems = navItems,
        selectedDestination = selectedDestination,
        onNavigate = onNavigate
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 14.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                PageIntro(
                    if (isAdmin) "Antrean pesanan" else "Pesanan saya",
                    "Pantau status pesanan kantin secara langsung."
                )
            }

            if (!isAdmin) {
                item {
                    PrimaryAction("Pesan makanan", onCreateOrder)
                }
            }

            if (orders.isEmpty()) {
                item {
                    InfoPanel(
                        "Belum ada pesanan",
                        "Pesanan yang kamu buat akan muncul di halaman ini."
                    )
                }
            }

            items(orders, key = { it.id }) { order ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOpenOrder(order.id)
                        },
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Pesanan #${order.queueNumber}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            StatusLabel(order.status.label)
                        }

                        Text(
                            order.menuName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            order.standName,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${order.quantity} porsi · ${order.fulfillment.label}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                formatRupiah(order.unitPrice * order.quantity),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            item {
                InfoPanel(
                    "Informasi",
                    "Data pesanan saat ini menggunakan penyimpanan dummy lokal."
                )
            }
        }
    }
}

@Composable
fun OrderDetailScreen(
    order: CustomerOrder?,
    onBack: () -> Unit,
    onReview: () -> Unit
) {
    CanteenScaffold(
        "Detail pesanan",
        onBack = onBack
    ) { padding ->

        if (order == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PageIntro(
                    "Pesanan tidak ditemukan",
                    "Data pesanan tidak tersedia."
                )
            }
            return@CanteenScaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PageIntro(
                "Pesanan #${order.queueNumber}",
                "${order.standName} · ${order.createdAt}"
            )

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        order.menuName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        "${order.quantity} × ${formatRupiah(order.unitPrice)}"
                    )

                    Text(
                        "Jenis pesanan · ${order.fulfillment.label}"
                    )

                    if (order.notes.isNotBlank()) {
                        Text("Catatan · ${order.notes}")
                    }

                    Text(
                        "Total ${formatRupiah(order.unitPrice * order.quantity)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                "Status pesanan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            val steps = listOf(
                OrderStatus.WAITING,
                OrderStatus.PREPARING,
                OrderStatus.READY,
                OrderStatus.COMPLETED
            )

            val currentIndex = steps.indexOf(order.status)

            if (order.status == OrderStatus.CANCELLED) {
                StatusLabel(order.status.label)
            } else {
                steps.forEachIndexed { index, status ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            if (currentIndex >= index) "●" else "○",
                            color = if (currentIndex >= index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            }
                        )

                        Column {
                            Text(
                                status.label,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            if (index == currentIndex) {
                                Text(
                                    "Status saat ini",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            if (order.status == OrderStatus.COMPLETED) {
                PrimaryAction("Beri ulasan", onReview)
            }
        }
    }
}

@Composable
fun OrderFormScreen(
    menuName: String,
    standName: String,
    unitPrice: Int,
    onBack: () -> Unit,
    onSubmit: (
        quantity: Int,
        fulfillment: FulfillmentType,
        notes: String
    ) -> Unit
) {
    var quantity by rememberSaveable {
        mutableStateOf("1")
    }

    var notes by rememberSaveable {
        mutableStateOf("")
    }

    var fulfillment by rememberSaveable {
        mutableStateOf(FulfillmentType.TAKEAWAY)
    }

    val parsedQuantity = quantity.toIntOrNull() ?: 0
    val validQuantity = parsedQuantity > 0

    CanteenScaffold(
        "Buat pesanan",
        onBack = onBack
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PageIntro(
                "Konfirmasi pesanan",
                "Periksa pilihan sebelum dikirim ke stand."
            )

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        menuName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        standName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        formatRupiah(unitPrice),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            OutlinedTextField(
                value = quantity,
                onValueChange = {
                    quantity = it.filter { char -> char.isDigit() }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Jumlah porsi") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Text(
                "Cara menikmati",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    FulfillmentType.TAKEAWAY,
                    FulfillmentType.DINE_IN
                ).forEach { option ->

                    FilterChip(
                        selected = fulfillment == option,
                        onClick = {
                            fulfillment = option
                        },
                        label = {
                            Text(option.label)
                        }
                    )
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Catatan untuk stand") },
                minLines = 2
            )

            InfoPanel(
                "Pembayaran",
                "Saldo CanteenPay akan tersedia setelah integrasi pembayaran."
            )

            Spacer(Modifier.weight(1f))

            Text(
                "Total: ${
                    if (validQuantity) {
                        formatRupiah(unitPrice * parsedQuantity)
                    } else {
                        "Masukkan jumlah porsi yang valid"
                    }
                }",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            PrimaryAction(
                "Konfirmasi pesanan",
                onClick = {
                    if (validQuantity && unitPrice > 0) {
                        onSubmit(
                            parsedQuantity,
                            fulfillment,
                            notes.trim()
                        )
                    }
                }
            )
        }
    }
}
