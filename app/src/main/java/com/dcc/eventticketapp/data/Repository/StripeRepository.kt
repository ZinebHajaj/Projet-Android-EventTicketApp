package com.dcc.eventticketapp.data.Repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StripeRepository @Inject constructor() {

    private val secretKey = ""
    private val client = OkHttpClient()

    suspend fun createPaymentIntent(
        amountInCents: Int,
        currency: String = "eur"
    ): String = withContext(Dispatchers.IO) {

        val body = "amount=$amountInCents&currency=$currency"
            .toRequestBody("application/x-www-form-urlencoded".toMediaType())

        val request = Request.Builder()
            .url("https://api.stripe.com/v1/payment_intents")
            .addHeader("Authorization", "Bearer $secretKey")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""
        val json = JSONObject(responseBody)

        if (!response.isSuccessful) {
            throw Exception("Stripe error: ${json.optString("error")}")
        }

        json.getString("client_secret")
    }
}