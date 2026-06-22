package com.dcc.eventticketapp.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.TicketModel
import com.dcc.eventticketapp.data.Entities.TicketStatus
import com.dcc.eventticketapp.data.Preferences.CurrencyPreference
import com.dcc.eventticketapp.data.Repository.AuthRepository
import com.dcc.eventticketapp.data.Repository.EventRepository
import com.dcc.eventticketapp.data.Repository.PayPalRepository
import com.dcc.eventticketapp.data.Repository.StripeRepository
import com.dcc.eventticketapp.data.Repository.TicketRepository
import com.dcc.eventticketapp.utils.CurrencyConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val eventRepository  : EventRepository,
    private val ticketRepository : TicketRepository,
    private val authRepository   : AuthRepository,
    private val currencyPreference : CurrencyPreference,
    private val stripeRepository   : StripeRepository,
    private val payPalRepository   : PayPalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookingViewState())
    val state: StateFlow<BookingViewState> = _state

    init {
        viewModelScope.launch {
            currencyPreference.currencyFlow.collect { currency ->
                _state.value = _state.value.copy(currency = currency)
            }
        }
    }

    fun handleIntent(intent: BookingIntent) {
        when (intent) {

            is BookingIntent.LoadEvent -> {
                viewModelScope.launch { loadEvent(intent.eventId) }
            }

            is BookingIntent.SelectTier -> {
                _state.value = _state.value.copy(selectedTierId = intent.tierId)
            }

            is BookingIntent.SelectSlot -> {
                _state.value = _state.value.copy(selectedSlotId = intent.slotId)
            }

            is BookingIntent.IncreaseQuantity -> {
                val maxAvailable = _state.value.selectedTier?.availableSeats
                    ?: _state.value.selectedSlot?.available
                    ?: intent.max
                val limit = minOf(intent.max, maxAvailable)
                if (_state.value.quantity < limit) {
                    _state.value = _state.value.copy(quantity = _state.value.quantity + 1)
                }
            }

            is BookingIntent.DecreaseQuantity -> {
                if (_state.value.quantity > 1) {
                    _state.value = _state.value.copy(quantity = _state.value.quantity - 1)
                }
            }

            is BookingIntent.PromoCodeChanged -> {
                _state.value = _state.value.copy(
                    promoCode  = intent.code.uppercase(),
                    promoError = null
                )
            }

            is BookingIntent.ApplyPromoCode -> {
                applyPromoCode()
            }

            is BookingIntent.CardNumberChanged -> {
                val digits = intent.number.filter { it.isDigit() }.take(16)
                _state.value = _state.value.copy(cardNumber = digits)
            }

            is BookingIntent.CardHolderChanged -> {
                _state.value = _state.value.copy(cardHolder = intent.name)
            }

            is BookingIntent.ExpiryChanged -> {
                val digits = intent.expiry.filter { it.isDigit() }.take(4)
                val formatted = if (digits.length >= 3)
                    "${digits.take(2)}/${digits.drop(2)}"
                else digits
                _state.value = _state.value.copy(expiry = formatted)
            }

            is BookingIntent.CvvChanged -> {
                val digits = intent.cvv.filter { it.isDigit() }.take(3)
                _state.value = _state.value.copy(cvv = digits)
            }

            is BookingIntent.ProceedToPayment -> {
                _state.value = _state.value.copy(
                    currentStep = BookingStep.PAYMENT,
                    reservationExpiryMillis = System.currentTimeMillis() + (2 * 60 * 60 * 1000)
                )
            }

            is BookingIntent.ConfirmPayment -> {
                viewModelScope.launch { confirmPayment() }
            }

            is BookingIntent.ResetState -> {
                _state.value = BookingViewState()
            }

            is BookingIntent.ChangeCurrency -> {
                viewModelScope.launch {
                    currencyPreference.setCurrency(intent.currency)
                }
            }

            is BookingIntent.SelectPaymentMethod -> {
                _state.value = _state.value.copy(selectedPaymentMethod = intent.method)
            }

            is BookingIntent.StartReservationTimer -> {
                _state.value = _state.value.copy(
                    reservationExpiryMillis = System.currentTimeMillis() + (2 * 60 * 60 * 1000) // 2h
                )
            }

            is BookingIntent.StripeError -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error     = intent.message
                )
            }

        }
    }

    private suspend fun loadEvent(eventId: String) {
        _state.value = _state.value.copy(isLoading = true)

        val userId = authRepository.currentUserId()
        if (userId == null) {
            _state.value = _state.value.copy(
                isLoading    = false,
                requiresAuth = true
            )
            return
        }

        try {
            val event = eventRepository.getEventById(eventId)
                ?: throw Exception("Événement introuvable")

            if (event.requiresSeatSelection) {
                val tiers = eventRepository.generateSeatTiers(eventId, event.priceStandard)
                _state.value = _state.value.copy(
                    isLoading      = false,
                    event          = event,
                    seatTiers      = tiers,
                    selectedTierId = tiers.firstOrNull { !it.isSoldOut }?.tierId
                )
            } else {
                val slots = eventRepository.generateTimeSlots()
                _state.value = _state.value.copy(
                    isLoading      = false,
                    event          = event,
                    timeSlots      = slots,
                    selectedSlotId = slots.firstOrNull { !it.isFull }?.slotId
                )
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false, error = e.message)
        }
    }

    private fun applyPromoCode() {
        val code = _state.value.promoCode

        val discount = when (code) {
            "WELCOME10" -> 0.10
            "VIP20"     -> 0.20
            "STUDENT15" -> 0.15
            else        -> null
        }

        if (discount != null) {
            _state.value = _state.value.copy(
                promoApplied  = true,
                promoDiscount = discount,
                promoError    = null
            )
        } else {
            _state.value = _state.value.copy(
                promoApplied  = false,
                promoDiscount = 0.0,
                promoError    = "Code promo invalide"
            )
        }
    }

    internal suspend fun confirmPayment() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val event    = _state.value.event ?: throw Exception("Événement introuvable")
            val userId   = authRepository.currentUserId() ?: "guest"

            val tierId = _state.value.selectedTierId
            val slotId = _state.value.selectedSlotId

            if (tierId != null) {
                // Vérifier places pour les événements avec sièges
                val available = ticketRepository.getAvailableSeats(event.id, tierId)
                if (available < _state.value.quantity) {
                    throw Exception("Il ne reste que $available place(s) disponible(s)")
                }
                // Incrémenter le compteur
                ticketRepository.incrementBookedSeats(event.id, tierId, _state.value.quantity)
            }

            val ticketId = UUID.randomUUID().toString().take(8).uppercase()

            val tierLabel = _state.value.selectedTier?.name
                ?: _state.value.selectedSlot?.time?.let { "Créneau $it" }
                ?: "Standard"

            val ticket = TicketModel(
                ticketId   = ticketId,
                eventTitle = "${event.title} — $tierLabel",
                eventDate  = event.date,
                eventCity  = event.city,
                eventImage = event.imageUrl,
                category   = event.category,
                price = _state.value.totalPrice,
                currency   = _state.value.currency,
                qrCode     = "TICKET-$ticketId-${event.id}-$userId",
                status     = TicketStatus.VALID
            )

            ticketRepository.saveTicket(ticket, userId)

            _state.value = _state.value.copy(
                isLoading   = false,
                isSuccess   = true,
                ticketId    = ticketId,
                currentStep = BookingStep.SUCCESS
            )

        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error     = e.message ?: "Erreur lors du paiement"
            )
        }
    }

    fun createPaymentIntent(onReady: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val amountInEur = CurrencyConverter.convert(_state.value.totalPrice, "EUR")
                val amountInCents = (amountInEur * 100).toInt()
                val clientSecret = stripeRepository.createPaymentIntent(amountInCents, "eur")
                _state.value = _state.value.copy(isLoading = false)
                onReady(clientSecret)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erreur Stripe"
                )
            }
        }
    }

    fun createPayPalOrder(onReady: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val amountInEur = CurrencyConverter.convert(_state.value.totalPrice, "EUR")
                val orderId = payPalRepository.createOrder(amountInEur)
                _state.value = _state.value.copy(
                    isLoading   = false,
                    payPalOrderId = orderId
                )
                onReady(orderId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error     = e.message ?: "Erreur PayPal"
                )
            }
        }
    }

    fun capturePayPalOrder(orderId: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val success = payPalRepository.captureOrder(orderId)
                if (success) {
                    confirmPayment()
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error     = "Paiement PayPal échoué"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error     = e.message ?: "Erreur PayPal"
                )
            }
        }
    }

}