package com.uinantasari.kampuscare.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uinantasari.kampuscare.databinding.ActivityRincianBinding
import com.uinantasari.kampuscare.model.Note
import com.uinantasari.kampuscare.viewmodel.NoteViewModel
import com.uinantasari.kampuscare.KampusCareApp

/**
 * Activity Rincian menampilkan detail lengkap sebuah catatan fasilitas kampus.
 * Berisi informasi judul, kategori, lokasi, deskripsi, dan tombol Ubah/Hapus.
 */
class RincianActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRincianBinding
    private lateinit var noteViewModel: NoteViewModel

    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRincianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteViewModel = NoteViewModel(KampusCareApp.getInstance().repository)

        noteId = intent.getIntExtra("NOTE_ID", -1)

        if (noteId != -1) {
            loadNote()
        } else {
            finish()
        }
    }

    private fun loadNote() {
        noteViewModel.notes.observe(this) { notes ->
            val note = notes.find { it.id == noteId }
            if (note != null) {
                binding.tvJudul.text = note.judul
                binding.tvKategori.text = note.kategori
                binding.tvLokasi.text = note.lokasi
                binding.tvDeskripsi.text = note.deskripsi
                binding.tvWaktu.text = java.text.DateFormat.getDateTimeInstance(
                    java.text.DateFormat.MEDIUM,
                    java.text.DateFormat.SHORT
                ).format(note.waktuDibuat)

                binding.btnUbah.setOnClickListener {
                    val intent = Intent(this, com.uinantasari.kampuscare.activity.FormActivity::class.java)
                    intent.putExtra("NOTE_ID", note.id)
                    startActivity(intent)
                }

                binding.btnHapus.setOnClickListener {
                    showDeleteConfirmDialog()
                }
            }
        }
    }

    private fun showDeleteConfirmDialog() {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle(R.string.hapus_title)
            .setMessage(R.string.hapus_message)
            .setPositiveButton(R.string.hapus) { _, _ ->
                noteViewModel.deleteNote(noteId)
                finish()
            }
            .setNegativeButton(R.string.batal, null)
            .create()
        dialog.show()
    }
}
