package com.aktanberdi.creditcalculatorbyaqtanb

class InstallmentCredit(
    private val amount: Double,
    private val months: Int
) : Credit {
    override fun calculatePayment(): Double {
        return amount / months
    }

    override fun calculateOverpayment(): Double {
        return 0.0
    }

    override fun calculateTotalAmount(): Double {
        return calculatePayment() * months
    }
}
