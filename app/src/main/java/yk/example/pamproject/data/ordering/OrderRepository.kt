
package yk.example.pamproject.data.ordering

import yk.example.pamproject.domain.ordering.CustomerOrder
import yk.example.pamproject.domain.ordering.OrderStatus
import yk.example.pamproject.domain.ordering.FulfillmentType

class OrderRepository {

    private val orders = mutableListOf(
        CustomerOrder(
            id = "ORD001",
            queueNumber = "A001",
            menuName = "Nasi Ayam Sambal Matah",
            standName = "Kantin Bu Sari",
            unitPrice = 18000,
            quantity = 2,
            fulfillment = FulfillmentType.TAKEAWAY,
            notes = "Sambal dipisah",
            estimatedPickup = "12:30",
            createdAt = "18 November 2026",
            status = OrderStatus.PREPARING
        ),
        CustomerOrder(
            id = "ORD002",
            queueNumber = "A002",
            menuName = "Es Teh Manis",
            standName = "Kantin Bu Sari",
            unitPrice = 5000,
            quantity = 1,
            fulfillment = FulfillmentType.DINE_IN,
            notes = "",
            estimatedPickup = "12:40",
            createdAt = "18 November 2026",
            status = OrderStatus.READY
        ),
        CustomerOrder(
            id = "ORD003",
            queueNumber = "A003",
            menuName = "Mie Ayam",
            standName = "Kantin Pak Budi",
            unitPrice = 15000,
            quantity = 1,
            fulfillment = FulfillmentType.TAKEAWAY,
            notes = "Tidak pedas",
            estimatedPickup = "11:45",
            createdAt = "18 November 2026",
            status = OrderStatus.COMPLETED
        )
    )

    fun getAllOrders(): List<CustomerOrder> {
        return orders.toList()
    }

    fun addOrder(order: CustomerOrder) {
        orders.add(0, order)
    }

    fun cancelOrder(orderId: String): Boolean {
        val index = orders.indexOfFirst { it.id == orderId }

        if (index == -1) return false

        val order = orders[index]

        if (order.status != OrderStatus.WAITING) {
            return false
        }

        orders[index] = order.copy(
            status = OrderStatus.CANCELLED
        )

        return true
    }

    fun updateOrderStatus(
        orderId: String,
        newStatus: OrderStatus
    ): Boolean {
        val index = orders.indexOfFirst { it.id == orderId }

        if (index == -1) return false

        orders[index] = orders[index].copy(
            status = newStatus
        )

        return true
    }
}
