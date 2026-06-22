package com.dcc.eventticketapp.ui.booking.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.dcc.eventticketapp.ui.theme.OrangeMain

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPalWebViewScreen(
    orderId   : String,
    clientId  : String,
    amount    : Double,
    onSuccess : (String) -> Unit,
    onCancel  : () -> Unit,
    onBack    : () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    val approvalUrl = "https://www.sandbox.paypal.com/checkoutnow?token=$orderId"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("PayPal", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, "Retour",
                            tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                                if (url != null) {
                                    when {
                                        url.contains("example.com/success") ||
                                                url.contains("PayerID") -> {
                                            val payerId = url
                                                .substringAfter("PayerID=")
                                                .substringBefore("&")
                                            onSuccess(payerId)
                                        }
                                        url.contains("example.com/cancel") -> {
                                            onCancel()
                                        }
                                    }
                                }
                            }
                        }
                        loadUrl(approvalUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                CircularProgressIndicator(
                    color    = OrangeMain,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}