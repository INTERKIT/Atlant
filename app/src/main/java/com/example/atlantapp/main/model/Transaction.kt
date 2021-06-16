package com.example.atlantapp.main.model

import java.math.BigInteger

data class Transaction(
    val address: String,
    val value: BigInteger
) {

    @Suppress("MagicNumber")
    fun getFormattedAddress(): String {
        if (address.isEmpty()) {
            return "N/A"
        }

        val firstSix = address.take(4)
        val lastFour = address.takeLast(4)
        return "$firstSix...$lastFour"
    }
}
