package yk.example.pamproject.data.canteen_pay

import yk.example.pamproject.domain.canteen_pay.model.Balance
import yk.example.pamproject.domain.canteen_pay.repository.BalanceRepository

/**
 * Implementasi sementara [BalanceRepository] yang hanya membaca data dummy.
 */
class DummyBalanceRepository : BalanceRepository {
    override fun getBalance(): Balance = DummyBalanceData.balance
}
