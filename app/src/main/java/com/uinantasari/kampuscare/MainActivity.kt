package com.uinantasari.kampuscare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uinantasari.kampuscare.databinding.ActivityMainBinding

/**
 * MainActivity menjadi aktivitas induk yang mengelola fragment-hal berikut:
 * - Beranda (home screen)
 * - Daftar (catatan daftar)
 * - Rincian (detail catatan)
 * - Pengaturan (pengaturan aplikasi)
 *
 * Formulir tambah/ubah diselenggarakan di FormActivity terpisah.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, BerandaFragment())
            .commit()
    }
}
