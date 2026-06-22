package com.dcc.eventticketapp.ui.booking

import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Entities.SeatTier
import com.dcc.eventticketapp.data.Entities.TimeSlot

data class BookingViewState(

    // État général
    val isLoading   : Boolean     = false,
    val error       : String?     = null,

    // Événement
    val event       : EventModel? = null,

    // Sièges (Concerts, Sports, Théâtre)
    val seatTiers       : List<SeatTier> = emptyList(),
    val selectedTierId  : String?        = null,

    // Créneaux (Ateliers, Autres)
    val timeSlots        : List<TimeSlot> = emptyList(),
    val selectedSlotId   : String?        = null,

    // Sélection
    val quantity    : Int         = 1,

    // Code promo
    val promoCode    : String  = "",
    val promoApplied : Boolean = false,
    val promoDiscount : Double = 0.0,
    val promoError    : String? = null,

    // Paiement
    val cardNumber  : String      = "",
    val cardHolder  : String      = "",
    val expiry      : String      = "",
    val cvv         : String      = "",
    val currency : String = "MAD",
    val selectedPaymentMethod : PaymentMethod = PaymentMethod.CARD,
    val reservationExpiryMillis : Long = 0L,
    val payPalOrderId : String = "",

    // Étapes
    val currentStep : BookingStep = BookingStep.SUMMARY,

    // Résultat
    val isSuccess   : Boolean     = false,
    val ticketId    : String      = "",

    // Erreur d'authentification
    val requiresAuth : Boolean = false
) {

    fun formattedPrice(amountInMad: Double): String {
        return com.dcc.eventticketapp.utils.CurrencyConverter.formatPrice(amountInMad, currency)
    }

    val selectedTier: SeatTier?
        get() = seatTiers.find { it.tierId == selectedTierId }

    val selectedSlot: TimeSlot?
        get() = timeSlots.find { it.slotId == selectedSlotId }

    // Prix unitaire selon le type d'événement
    val unitPrice: Double
        get() = selectedTier?.price ?: event?.priceStandard ?: 0.0

    val subtotal: Double
        get() = unitPrice * quantity

    val serviceFee: Double
        get() = subtotal * 0.05  // 5% frais de service

    val discountAmount: Double
        get() = if (promoApplied) subtotal * promoDiscount else 0.0

    val totalPrice: Double
        get() = subtotal + serviceFee - discountAmount

    val isPaymentValid: Boolean
        get() = cardNumber.length == 16 &&
                cardHolder.isNotBlank() &&
                expiry.length == 5 &&
                cvv.length == 3

    // Peut-on continuer vers le paiement ?
    val canProceed: Boolean
        get() = if (event?.requiresSeatSelection == true)
            selectedTierId != null
        else
            selectedSlotId != null
}

enum class BookingStep {
    SUMMARY,
    PAYMENT,
    SUCCESS
}

enum class PaymentMethod {
    CARD, PAYPAL
}