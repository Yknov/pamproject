package yk.example.pamproject.domain.canteen_pay.repository

import yk.example.pamproject.domain.canteen_pay.model.Balance

interface BalanceRepository {
    fun getBalance(): Balance
}
