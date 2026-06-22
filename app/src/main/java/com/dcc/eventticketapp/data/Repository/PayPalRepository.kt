package com.dcc.eventticketapp.data.Repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PayPalRepository @Inject constructor() {

    // Mode Sandbox uniquement
    private val clientId     = "AX2a5PCwd-BoHbIzU4tuEDH_hWmksGmux0cba2S5ZNR74uiBtNSFR7m-Y9WegyroEXCll5bbootkdo66"
    private val clientSecret = "ED_P8BzdfPKjMAd_rm4CekUimJWVwiFoBa95OsYnd-m8FPVqkh42h6l2fzer1wiPuqyroNmDIvfzFgGh"
    private val baseUrl      = "https://api-m.sandbox.paypal.com"

    private val client = OkHttpClient()

    // ── 1. Obtenir le token d'accès ──────────────────────────────
    private suspend fun getAccessToken(): String = withContext(Dispatchers.IO) {
        val credentials = Credentials.basic(clientId, clientSecret)

        val body = "grant_type=client_credentials"
            .toRequestBody("application/x-www-form-urlencoded".toMediaType())

        val request = Request.Builder()
            .url("$baseUrl/v1/oauth2/token")
            .addHeader("Authorization", credentials)
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        val json     = JSONObject(response.body?.string() ?: "")

        json.getString("access_token")
    }

    // ── 2. Créer une commande PayPal ─────────────────────────────
    suspend fun createOrder(amountInEur: Double): String = withContext(Dispatchers.IO) {

        val accessToken = getAccessToken()

        val orderBody = JSONObject().apply {
            put("intent", "CAPTURE")
            put("purchase_units", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("amount", JSONObject().apply {
                        put("currency_code", "EUR")
                        put("value", "%.2f".format(amountInEur).replace(",", "."))
                    })
                })
            })
            // ← ajoute ça
            put("application_context", JSONObject().apply {
                put("return_url", "https://example.com/success")
                put("cancel_url", "https://example.com/cancel")
                put("brand_name", "TicketGo")
                put("user_action", "PAY_NOW")
            })
        }.toString()

        val request = Request.Builder()
            .url("$baseUrl/v2/checkout/orders")
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("Content-Type", "application/json")
            .post(orderBody.toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        android.util.Log.d("PAYPAL", "Response: $responseBody")

        val json = JSONObject(responseBody)

        if (!response.isSuccessful) {
            throw Exception("PayPal error: $responseBody")
        }

        json.getString("id")
    }

    // ── 3. Capturer le paiement ──────────────────────────────────
    suspend fun captureOrder(orderId: String): Boolean = withContext(Dispatchers.IO) {

        val accessToken = getAccessToken()

        val request = Request.Builder()
            .url("$baseUrl/v2/checkout/orders/$orderId/capture")
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("Content-Type", "application/json")
            .post("{}".toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()
        val json     = JSONObject(response.body?.string() ?: "")

        json.optString("status") == "COMPLETED"
    }
}