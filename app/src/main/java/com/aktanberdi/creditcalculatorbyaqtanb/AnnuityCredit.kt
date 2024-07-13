package com.aktanberdi.creditcalculatorbyaqtanb

import kotlin.math.pow

class AnnuityCredit(
    private val amount: Double,
    private val interestRate: Double,
    private val months: Int
) : Credit {
    override fun calculatePayment(): Double {
        val monthlyInterestRate = interestRate / 12 / 100
        return amount * monthlyInterestRate / (1 - (1 + monthlyInterestRate).pow(-months.toDouble()))
    }

    override fun calculateOverpayment(): Double {
        return calculateTotalAmount() - amount
    }

    override fun calculateTotalAmount(): Double {
        return calculatePayment() * months
    }
}
