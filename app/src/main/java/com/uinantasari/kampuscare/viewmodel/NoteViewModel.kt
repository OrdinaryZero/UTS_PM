package com.uinantasari.kampuscare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.uinantasari.kampuscare.model.Note
import com.uinantasari.kampuscare.repository.NoteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola state UI layer catatan.
 * Memisahkan logika bisnis dari View (Activity/Fragment).
 */
class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _notes = repository.notes.asLiveData()
    val notes: LiveData<List<Note>> = _notes

    private val _totalNotes = MutableLiveData<Int>()
    val totalNotes: LiveData<Int> = _totalNotes

    private val _selectedNote = MutableLiveData<Note?>()
    val selectedNote: LiveData<Note?> = _selectedNote

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getTotalNotes() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _totalNotes.value = repository.getTotalNotes()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                repository.addNote(note)
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                repository.updateNote(note)
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message
            }
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                repository.deleteNote(id)
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message
            }
        }
    }

    fun setSelectedNote(note: Note?) {
        _selectedNote.value = note
    }
}
