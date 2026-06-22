package com.dcc.eventticketapp.utils

object CurrencyConverter {

    private val rates = mapOf(
        "MAD" to 1.0,
        "EUR" to 0.092,
        "USD" to 0.099
    )

    private val symbols = mapOf(
        "MAD" to "MAD",
        "EUR" to "€",
        "USD" to "$"
    )

    fun convert(amountInMad: Double, toCurrency: String): Double {
        val rate = rates[toCurrency] ?: 1.0
        return amountInMad * rate
    }

    // Fonction unique - convertit TOUJOURS de MAD vers la devise cible
    fun formatPrice(amountInMad: Double, currency: String): String {
        val converted = convert(amountInMad, currency)
        val symbol = symbols[currency] ?: currency

        return when (currency) {
            "MAD" -> "${"%.2f".format(converted)} $symbol"
            else  -> "$symbol${"%.2f".format(converted)}"
        }
    }

    fun availableCurrencies() = listOf("MAD", "EUR", "USD")
}