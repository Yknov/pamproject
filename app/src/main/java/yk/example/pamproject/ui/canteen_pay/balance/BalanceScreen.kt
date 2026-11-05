package yk.example.pamproject.ui.canteen_pay.balance

import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale
import yk.example.pamproject.data.canteen_pay.DummyBalanceData
import yk.example.pamproject.data.canteen_pay.DummyBalanceRepository
import yk.example.pamproject.domain.canteen_pay.model.Balance
import yk.example.pamproject.domain.canteen_pay.model.BalanceTransaction
import yk.example.pamproject.ui.theme.PamprojectTheme

/**
 * Entry point layar saldo CanteenPay.
 *
 * Saat ini data diambil dari [DummyBalanceRepository]. Nanti tinggal diganti
 * dengan repository yang terhubung ke sumber data sebenarnya.
 */
@Composable
fun BalanceRoute(modifier: Modifier = Modifier) {
    val repository = remember { DummyBalanceRepository() }
    val balance = remember(repository) { repository.getBalance() }
    val context = LocalContext.current

    BalanceScreen(
        balance = balance,
        onTopUpClick = {
            Toast.makeText(context, "Fitur top up belum tersedia", Toast.LENGTH_SHORT).show()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    balance: Balance,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "CanteenPay") })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            BalanceCard(
                amount = balance.amount,
                onTopUpClick = onTopUpClick,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Riwayat Transaksi",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (balance.transactions.isEmpty()) {
                Text(
                    text = "Belum ada transaksi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                balance.transactions.forEach { transaction ->
                    BalanceTransactionItem(transaction = transaction)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun BalanceCard(
    amount: Long,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Saldo CanteenPay",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatRupiah(amount),
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onTopUpClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Top Up")
            }
        }
    }
}

@Composable
private fun BalanceTransactionItem(
    transaction: BalanceTransaction,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = formatSignedRupiah(transaction.amount),
                style = MaterialTheme.typography.titleSmall,
                color = if (transaction.amount >= 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
            )
        }
    }
}

private val rupiahFormatter: NumberFormat =
    NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))

private fun formatRupiah(amount: Long): String = "Rp${rupiahFormatter.format(amount)}"

private fun formatSignedRupiah(amount: Long): String {
    val sign = if (amount >= 0) "+" else "-"
    return "$sign Rp${rupiahFormatter.format(kotlin.math.abs(amount))}"
}

@Preview(showBackground = true)
@Composable
private fun BalanceScreenPreview() {
    PamprojectTheme {
        BalanceScreen(
            balance = DummyBalanceData.balance,
            onTopUpClick = {},
        )
    }
}
