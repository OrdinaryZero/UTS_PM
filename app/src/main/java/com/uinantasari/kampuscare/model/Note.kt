package com.uinantasari.kampuscare.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class untuk data catatan fasilitas kampus.
 * Cocok dengan spesifikasi UTS PM:
 * - id: Primary key otomatis, tetap sama saat diubah
 * - judul: 5-100 karakter
 * - kategori: Penerangan, Kebersihan, Perabot, Jaringan, Lainnya
 * - lokasi: 3-80 karakter
 * - deskripsi: 20-500 karakter
 * - waktuDibuat: otomatis diisi saat catatan pertama disimpan
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val kategori: String,
    val lokasi: String,
    val deskripsi: String,
    val waktuDibuat: Long
) {
    companion object {
        const val KATEGORI_CALON = listOf("Penerangan", "Kebersihan", "Perabot", "Jaringan", "Lainnya")
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (judul.isBlank()) {
            errors += "Judul wajib diisi (5-100 karakter)"
        } else if (judul.length < 5 || judul.length > 100) {
            errors += "Judul harus 5-100 karakter"
        }
        if (kategori.isBlank()) {
            errors += "Kategori wajib dipilih"
        } else if (!Note.KATEGORI_CALON.contains(kategori)) {
            errors += "Kategori tidak valid"
        }
        if (lokasi.isBlank()) {
            errors += "Lokasi wajib diisi (3-80 karakter)"
        } else if (lokasi.length < 3 || lokasi.length > 80) {
            errors += "Lokasi harus 3-80 karakter"
        }
        if (deskripsi.isBlank()) {
            errors += "Deskripsi wajib diisi (20-500 karakter)"
        } else if (deskripsi.length < 20 || deskripsi.length > 500) {
            errors += "Deskripsi harus 20-500 karakter"
        }
        return errors
    }
}
