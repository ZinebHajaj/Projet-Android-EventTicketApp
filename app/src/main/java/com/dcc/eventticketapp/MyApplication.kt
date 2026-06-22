package com.dcc.eventticketapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.stripe.android.PaymentConfiguration

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Stripe init
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51Tks67HBn60HADVm5g436kpiYkSKbmxNgxz1O0NTQS8PSIlrapcP0Y6rEWrgkK19xqKoUH3bJKdYhx81Iy58kfTS002ur3OBvh"
        )
    }
}