package com.uinantasari.kampuscare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uinantasari.kampuscare.model.Note
import com.uinantasari.kampuscare.repository.NoteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel khusus untuk form tambah/ubah catatan.
 * Mengelola state form dan validasi sebelum disimpan ke repository.
 */
class NoteFormViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _formData = MutableLiveData<Note?>()
    val formData: LiveData<Note?> = _formData

    private val _errors = MutableLiveData<List<String>>()
    val errors: LiveData<List<String>> = _errors

    private val _submitting = MutableLiveData<Boolean>()
    val submitting: LiveData<Boolean> = _submitting

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    fun loadExistingNote(id: Int) {
        viewModelScope.launch {
            try {
                val note = repository.findById(id)
                _formData.value = note
            } catch (e: Exception) {
                _errors.value = listOf(e.message ?: "Gagal memuat data")
            }
        }
    }

    fun validateAndPrepareData(
        judul: String,
        kategori: String,
        lokasi: String,
        deskripsi: String
    ): List<String> {
        val errors = mutableListOf<String>()

        if (judul.isBlank()) {
            errors += "Judul wajib diisi."
        } else if (judul.length < 5 || judul.length > 100) {
            errors += "Judul harus 5-100 karakter."
        }

        val validKategori = listOf("Penerangan", "Kebersihan", "Perabot", "Jaringan", "Lainnya")
        if (kategori.isBlank()) {
            errors += "Kategori wajib dipilih."
        } else if (!validKategori.contains(kategori)) {
            errors += "Kategori tidak valid."
        }

        if (lokasi.isBlank()) {
            errors += "Lokasi wajib diisi (3-80 karakter)."
        } else if (lokasi.length < 3 || lokasi.length > 80) {
            errors += "Lokasi harus 3-80 karakter."
        }

        if (deskripsi.isBlank()) {
            errors += "Deskripsi wajib diisi (20-500 karakter)."
        } else if (deskripsi.length < 20 || deskripsi.length > 500) {
            errors += "Deskripsi harus 20-500 karakter."
        }

        _errors.value = errors
        return errors
    }

    fun submitNote(judul: String, kategori: String, lokasi: String, deskripsi: String, id: Int) {
        val errors = validateAndPrepareData(judul, kategori, lokasi, deskripsi)
        if (errors.isNotEmpty()) {
            _errors.value = errors
            return
        }

        viewModelScope.launch {
            try {
                _submitting.value = true
                _errors.value = emptyList()
                _successMessage.value = null

                val note = Note(
                    id = if (id == 0) 0 else id,
                    judul = judul.trim(),
                    kategori = kategori,
                    lokasi = lokasi.trim(),
                    deskripsi = deskripsi.trim(),
                    waktuDibuat = System.currentTimeMillis()
                )

                if (id == 0) {
                    repository.addNote(note)
                    _successMessage.value = "Catatan berhasil disimpan."
                } else {
                    repository.updateNote(note)
                    _successMessage.value = "Catatan berhasil diperbarui."
                }
            } catch (e: Exception) {
                _errors.value = listOf(e.message ?: "Terjadi kesalahan")
            } finally {
                _submitting.value = false
            }
        }
    }
}
