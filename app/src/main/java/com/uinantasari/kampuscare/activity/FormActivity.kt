package com.uinantasari.kampuscare.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uinantasari.kampuscare.databinding.ActivityFormBinding
import com.uinantasari.kampuscare.viewmodel.NoteFormViewModel
import com.uinantasari.kampuscare.KampusCareApp

/**
 * Activity FormActivity digunakan untuk menambah atau mengubah catatan fasilitas kampus.
 *
 * - Saat dibuka tanpa ID: mode "tambah" (tampilkan form kosong)
 * - Saat dibuka dengan ID: mode "ubah" (isi form dengan data catatan yang ada)
 *
 * Alur navigasi (spesifikasi UTS PM):
 * - Saat menyimpan berhasil, pengguna kembali ke layar sebelumnya
 * - Input tidak dikosongkan ketika validasi gagal
 * - Data berubah tetapi ID dan waktu pembuatan tetap terjaga
 */
class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding
    private lateinit var noteFormViewModel: NoteFormViewModel

    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteFormViewModel = NoteFormViewModel(KampusCareApp.getInstance().repository)

        // Ambil ID dari Intent
        noteId = intent.getIntExtra("NOTE_ID", 0)

        if (noteId != 0) {
            // Mode edit - load existing data
            noteFormViewModel.loadExistingNote(noteId)
        }

        setupToolbar()
        setupObservers()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionTitle(if (noteId != 0) R.string.ubah_catatan else R.string.tambah_catatan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupObservers() {
        noteFormViewModel.formData.observe(this) { note ->
            if (note != null) {
                binding.edtJudul.setText(note.judul)
                binding.spinnerKategori.setSelection(
                    com.uinantasari.kampuscare.model.Note.KATEGORI_CALON.indexOf(note.kategori) + 1
                )
                binding.edtLokasi.setText(note.lokasi)
                binding.edtDeskripsi.setText(note.deskripsi)
            }
        }

        noteFormViewModel.errors.observe(this) { errors ->
            binding.tvError.text = errors.takeIf { it.isNotEmpty() }?.joinToString("\n")
        }

        noteFormViewModel.successMessage.observe(this) { message ->
            if (message != null) {
                toast(message)
            }
        }

        noteFormViewModel.submitting.observe(this) { submitting ->
            binding.btnSimpan.isEnabled = !submitting
        }
    }

    private fun setupListeners() {
        binding.spinnerKategori.setOnItemSelectedListener(object :
            android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedPosition = position - 1
                val items = com.uinantasari.kampuscare.model.Note.KATEGORI_CALON
                if (selectedPosition in items.indices) {
                    binding.tvKategoriValue.text = items[selectedPosition]
                }
                parent?.setText(position, false)
                parent?.setSelection(position, false)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })

        binding.btnSimpan.setOnClickListener {
            val judul = binding.edtJudul.text.toString().trim()
            val kategori = binding.tvKategoriValue.text.toString().trim()
            val lokasi = binding.edtLokasi.text.toString().trim()
            val deskripsi = binding.edtDeskripsi.text.toString().trim()

            noteFormViewModel.submitNote(judul, kategori, lokasi, deskripsi, noteId)
        }

        binding.btnBatal.setOnClickListener {
            finish()
        }
    }
}
