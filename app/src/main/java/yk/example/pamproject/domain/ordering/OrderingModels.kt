
package yk.example.pamproject.domain.ordering

enum class OrderStatus(
    val label: String,
    val isHistory: Boolean
) {
    WAITING("Menunggu diproses", false),
    PREPARING("Sedang disiapkan", false),
    READY("Siap diambil", false),
    COMPLETED("Selesai", true),
    CANCELLED("Dibatalkan", true)
}

enum class FulfillmentType(val label: String) {
    TAKEAWAY("Takeaway"),
    DINE_IN("Dine-in")
}

data class CustomerOrder(
    val id: String,
    val queueNumber: String,
    val menuName: String,
    val standName: String,
    val unitPrice: Int,
    val quantity: Int,
    val fulfillment: FulfillmentType,
    val notes: String,
    val estimatedPickup: String,
    val createdAt: String,
    val status: OrderStatus
) {
    val subtotal: Int
        get() = unitPrice * quantity
}

fun Int.toRupiah(): String {
    return "Rp" + "%,d".format(this).replace(',', '.')
}
