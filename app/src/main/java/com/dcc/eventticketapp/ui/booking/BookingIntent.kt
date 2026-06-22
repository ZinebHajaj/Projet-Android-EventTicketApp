package com.dcc.eventticketapp.ui.booking

sealed class BookingIntent {

    data class LoadEvent(val eventId: String) : BookingIntent()

    // Sélection sièges ou créneaux
    data class SelectTier(val tierId: String) : BookingIntent()
    data class SelectSlot(val slotId: String) : BookingIntent()

    // Quantité
    data class IncreaseQuantity(val max: Int = 6) : BookingIntent()
    object DecreaseQuantity                         : BookingIntent()

    // Code promo
    data class PromoCodeChanged(val code: String) : BookingIntent()
    object ApplyPromoCode                          : BookingIntent()

    // Paiement
    data class CardNumberChanged(val number: String) : BookingIntent()
    data class CardHolderChanged(val name: String)   : BookingIntent()
    data class ExpiryChanged(val expiry: String)     : BookingIntent()
    data class CvvChanged(val cvv: String)           : BookingIntent()

    data class SelectPaymentMethod(val method: PaymentMethod) : BookingIntent()
    object StartReservationTimer : BookingIntent()

    object ProceedToPayment : BookingIntent()
    object ConfirmPayment   : BookingIntent()
    object ResetState       : BookingIntent()

    data class ChangeCurrency(val currency: String) : BookingIntent()

    data class StripeError(val message: String) : BookingIntent()
}