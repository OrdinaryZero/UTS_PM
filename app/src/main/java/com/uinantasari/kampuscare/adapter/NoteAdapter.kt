package com.uinantasari.kampuscare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uinantasari.kampuscare.R
import com.uinantasari.kampuscare.model.Note

/**
 * RecyclerView adapter untuk menampilkan daftar catatan fasilitas kampus.
 * Setiap item menampilkan judul, kategori, lokasi, dan waktu pembuatan.
 */
class NoteAdapter(
    private var notes: List<Note> = emptyList(),
    private val onNoteClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = notes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvJudul: TextView = itemView.findViewById(R.id.tvJudul)
        private val tvKategori: TextView = itemView.findViewById(R.id.tvKategori)
        private val tvLokasi: TextView = itemView.findViewById(R.id.tvLokasi)
        private val tvWaktu: TextView = itemView.findViewById(R.id.tvWaktu)

        fun bind(note: Note) {
            tvJudul.text = note.judul
            tvKategori.text = note.kategori
            tvLokasi.text = note.lokasi
            tvWaktu.text = java.text.DateFormat.getDateTimeInstance(
                java.text.DateFormat.MEDIUM,
                java.text.DateFormat.SHORT
            ).format(note.waktuDibuat)

            // Setup click listeners
            itemView.setOnClickListener { v ->
                v.performClick()
                onNoteClick(note)
            }
        }
    }
}
