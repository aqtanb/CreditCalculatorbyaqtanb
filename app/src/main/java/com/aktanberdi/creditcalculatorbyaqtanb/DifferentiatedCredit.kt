package com.aktanberdi.creditcalculatorbyaqtanb

class DifferentiatedCredit(
    private val amount: Double,
    private val interestRate: Double,
    private val months: Int
) : Credit {
    override fun calculatePayment(): Double {
        val monthlyInterestRate = interestRate / 12 / 100
        return amount / months + amount * monthlyInterestRate * (months - 1) / months
    }

    override fun calculateOverpayment(): Double {
        return calculateTotalAmount() - amount
    }

    override fun calculateTotalAmount(): Double {
        var total = 0.0
        for (month in 1..months) {
            total += calculatePayment(month)
        }
        return total
    }

    private fun calculatePayment(month: Int): Double {
        val monthlyInterestRate = interestRate / 12 / 100
        return amount / months + amount * monthlyInterestRate * (months - month) / months
    }
}
