package yk.example.pamproject.domain.canteen_pay.model

/**
 * Saldo dompet CanteenPay beserta riwayat mutasinya.
 */
data class Balance(
    val amount: Long,
    val transactions: List<BalanceTransaction> = emptyList(),
)

/**
 * Satu mutasi saldo.
 *
 * [amount] bernilai positif untuk top up dan negatif untuk pembayaran.
 */
data class BalanceTransaction(
    val id: String,
    val title: String,
    val date: String,
    val amount: Long,
    val type: BalanceTransactionType,
)

enum class BalanceTransactionType {
    TOP_UP,
    PAYMENT,
}
