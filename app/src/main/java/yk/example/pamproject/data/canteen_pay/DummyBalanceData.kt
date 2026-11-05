package yk.example.pamproject.data.canteen_pay

import yk.example.pamproject.domain.canteen_pay.model.Balance
import yk.example.pamproject.domain.canteen_pay.model.BalanceTransaction
import yk.example.pamproject.domain.canteen_pay.model.BalanceTransactionType

/**
 * Data dummy saldo CanteenPay.
 *
 * Dipakai sementara selama belum ada koneksi ke backend/Supabase.
 * Nilai saldo saat ini adalah hasil dari seluruh mutasi di bawahnya.
 */
object DummyBalanceData {

    val balance = Balance(
        amount = 45_000L,
        transactions = listOf(
            BalanceTransaction(
                id = "TRX-001",
                title = "Top Up Saldo",
                date = "05 Nov 2026, 08.30",
                amount = 50_000L,
                type = BalanceTransactionType.TOP_UP,
            ),
            BalanceTransaction(
                id = "TRX-002",
                title = "Pembayaran Pesanan #A12",
                date = "04 Nov 2026, 12.15",
                amount = -18_000L,
                type = BalanceTransactionType.PAYMENT,
            ),
            BalanceTransaction(
                id = "TRX-003",
                title = "Top Up Saldo",
                date = "03 Nov 2026, 19.05",
                amount = 25_000L,
                type = BalanceTransactionType.TOP_UP,
            ),
            BalanceTransaction(
                id = "TRX-004",
                title = "Pembayaran Pesanan #A08",
                date = "03 Nov 2026, 12.40",
                amount = -12_000L,
                type = BalanceTransactionType.PAYMENT,
            ),
        ),
    )
}
