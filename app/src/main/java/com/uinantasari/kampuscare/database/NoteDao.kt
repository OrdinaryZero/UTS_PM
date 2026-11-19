package com.uinantasari.kampuscare.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uinantasari.kampuscare.model.Note

/**
 * Data Access Object (DAO) untuk operasi CRUD pada tabel notes.
 * Memisahkan akses data dari layer lain sesuai arsitektur MVVM.
 */
@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<Note>): LongArray

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun findById(id: Int): Note?

    @Query("SELECT * FROM notes WHERE id = :id")
    fun observeById(id: Int): kotlinx.coroutines.flow.Flow<Note?>

    @Query("SELECT * FROM notes ORDER BY waktuDibuat DESC")
    fun observeAllDescending(): kotlinx.coroutines.flow.Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY waktuDibuat ASC")
    fun observeAllAscending(): kotlinx.coroutines.flow.Flow<List<Note>>

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Int): Int

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun countAll(): Int
}
