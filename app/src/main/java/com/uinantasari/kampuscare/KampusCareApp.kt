package com.uinantasari.kampuscare

import android.app.Application

/**
 * Application class untuk inisialisasi database dan komponen global.
 */
class KampusCareApp : Application() {
    companion object {
        private lateinit var instance: KampusCareApp
        fun getInstance(): KampusCareApp = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
