package yk.example.pamproject.ui.ordering

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

import yk.example.pamproject.data.ordering.OrderRepository
import yk.example.pamproject.domain.ordering.CustomerOrder
import yk.example.pamproject.domain.ordering.OrderStatus
import yk.example.pamproject.domain.ordering.FulfillmentType

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderingViewModel : ViewModel() {

    private val repository = OrderRepository()

    // Menyimpan daftar seluruh pesanan
    var orders by mutableStateOf(repository.getAllOrders())
        private set

    // Menyimpan ID pesanan terakhir yang dibuat
    var lastCreatedOrderId by mutableStateOf<String?>(null)
        private set

    // Membuat pesanan baru
    fun createOrder(
        menuName: String,
        standName: String,
        unitPrice: Int,
        quantity: Int,
        fulfillment: FulfillmentType,
        notes: String,
        estimatedPickup: String
    ): CustomerOrder? {

        // Validasi data pesanan
        if (menuName.isBlank()) return null
        if (standName.isBlank()) return null
        if (unitPrice <= 0) return null
        if (quantity <= 0) return null

        val nextNumber = orders.size + 1

        val order = CustomerOrder(
            id = "ORD${System.currentTimeMillis()}",
            queueNumber = "A${String.format("%03d", nextNumber)}",
            menuName = menuName,
            standName = standName,
            unitPrice = unitPrice,
            quantity = quantity,
            fulfillment = fulfillment,
            notes = notes,
            estimatedPickup = estimatedPickup,
            createdAt = SimpleDateFormat(
                "dd MMMM yyyy",
                Locale("id", "ID")
            ).format(Date()),
            status = OrderStatus.WAITING
        )

        // Menyimpan pesanan melalui repository
        repository.addOrder(order)

        // Memperbarui daftar pesanan
        refreshOrders()

        lastCreatedOrderId = order.id

        return order
    }

    // Mengambil semua pesanan
    fun getAllOrders(): List<CustomerOrder> {
        return orders
    }

    // Mengambil pesanan yang masih diproses
    fun getActiveOrders(): List<CustomerOrder> {
        return orders.filter {
            !it.status.isHistory
        }
    }

    // Mengambil pesanan yang sudah selesai atau dibatalkan
    fun getHistoryOrders(): List<CustomerOrder> {
        return orders.filter {
            it.status.isHistory
        }
    }

    // Mencari detail pesanan berdasarkan ID
    fun findOrder(orderId: String): CustomerOrder? {
        return orders.firstOrNull {
            it.id == orderId
        }
    }

    // Membatalkan pesanan
    fun cancelOrder(orderId: String): Boolean {

        val result = repository.cancelOrder(orderId)

        if (result) {
            refreshOrders()
        }

        return result
    }

    // Mengubah status pesanan
    fun updateOrderStatus(
        orderId: String,
        newStatus: OrderStatus
    ): Boolean {

        val result = repository.updateOrderStatus(
            orderId,
            newStatus
        )

        if (result) {
            refreshOrders()
        }

        return result
    }

    // Memperbarui state UI
    private fun refreshOrders() {
        orders = repository.getAllOrders()
    }
}
