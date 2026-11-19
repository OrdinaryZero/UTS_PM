package com.uinantasari.kampuscare.repository

import android.content.Context
import androidx.room.Room
import com.uinantasari.kampuscare.database.AppDatabase
import com.uinantasari.kampuscare.database.NoteDao
import com.uinantasari.kampuscare.model.Note
import com.uinantasari.kampuscare.utils.PreferenceManager
import kotlinx.coroutines.flow.Flow

/**
 * Repository untuk manajemen data catatan fasilitas kampus.
 * Memusatkan validasi dan akses DAO sesuai spesifikasi UTS PM.
 * - Validasi dilakukan di repository layer
 * - Integrasi data dari Room (database) dan SharedPreferences
 */
class NoteRepository(context: Context) {

    private val dao: NoteDao = AppDatabase.getInstance(context).noteDao()
    private val preferenceManager: PreferenceManager = PreferenceManager(context)

    /**
     * Memuat catatan terbaru. Menggunakan flow supaya ViewModel
     * dapat mengamati perubahan data secara real-time.
     */
    val notes: Flow<List<Note>> = dao.observeAllDescending()

    /**
     * Mencari catatan berdasarkan ID.
     */
    fun observeNoteById(id: Int): Flow<Note?> {
        return dao.observeById(id)
    }

    /**
     * Menambah catatan baru.
     * Mengembalikan ID yang berhasil disimpan.
     */
    suspend fun addNote(note: Note): Long {
        // Validasi di repository layer
        val errors = note.validate()
        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(errors.joinToString("; "))
        }
        note.waktuDibuat = System.currentTimeMillis()
        return dao.insert(note)
    }

    /**
     * Mengubah catatan yang sudah ada.
     * ID tetap sama, hanya field lain yang diperbarui.
     */
    suspend fun updateNote(note: Note) {
        val errors = note.validate()
        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(errors.joinToString("; "))
        }
        // Cek apakah ada perubahan (hindari update kosong)
        if (dao.findById(note.id) != null) {
            dao.update(note)
        }
    }

    /**
     * Menghapus catatan berdasarkan ID.
     * Mengembalikan jumlah record yang terhapus (0 atau 1).
     */
    suspend fun deleteNote(id: Int): Int {
        return dao.delete(id)
    }

    /**
     * Mendapatkan jumlah total catatan.
     */
    suspend fun getTotalNotes(): Int {
        return dao.countAll()
    }

    /**
     * Memilih urutan daftar: terbaru (DESC) atau terlama (ASC).
     */
    var listOrder: Int
        get() = preferenceManager.getInt(PreferenceManager.KEY_LIST_ORDER, 0) // 0 = terbaru, 1 = terlama
        set(value) {
            preferenceManager.putInt(PreferenceManager.KEY_LIST_ORDER, value)
        }
}
