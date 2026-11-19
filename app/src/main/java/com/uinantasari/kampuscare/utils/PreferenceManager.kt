package com.uinantasari.kampuscare.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Utility class untuk mengelola SharedPreferences.
 * Menyimpan preferensi aplikasi seperti pilihan urutan daftar.
 */
object PreferenceManager {
    const val KEY_LIST_ORDER = "key_list_order"
    const val DEFAULT_LIST_ORDER = 0 // 0 = terbaru, 1 = terlama

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("kampuscare_prefs", Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
}
