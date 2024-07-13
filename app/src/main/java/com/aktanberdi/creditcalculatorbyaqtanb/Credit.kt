package com.aktanberdi.creditcalculatorbyaqtanb

interface Credit {
    fun calculatePayment(): Double
    fun calculateOverpayment(): Double
    fun calculateTotalAmount(): Double
}