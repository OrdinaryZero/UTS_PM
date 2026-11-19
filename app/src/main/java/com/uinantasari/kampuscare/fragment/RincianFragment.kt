package com.uinantasari.kampuscare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uinantasari.kampuscare.R
import com.uinantasari.kampuscare.databinding.FragmentRincianBinding

/**
 * Fragment Rincian menampilkan detail lengkap sebuah catatan fasilitas kampus.
 * Menampilkan: judul, kategori, lokasi, deskripsi, dan waktu pembuatan.
 * Menyediakan tombol Ubah (navigasi ke FormActivity) dan Hapus.
 */
class RincianFragment : Fragment() {

    private var _binding: FragmentRincianBinding? = null
    private val binding get() = _binding!!

    private var noteId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRincianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getInt("NOTE_ID", -1) ?: -1
        if (noteId == -1) {
            requireActivity().finish()
            return
        }

        loadNote()
    }

    private fun loadNote() {
        val activity = requireActivity()
        val noteViewModel = NoteViewModel((activity.applicationContext as KampusCareApp).repository)

        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
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
                    val intent = requireActivity().intent
                    intent.putExtra("NOTE_ID", note.id)
                    startActivity(intent)
                }

                binding.btnHapus.setOnClickListener {
                    showDeleteConfirmDialog(note)
                }
            }
        }
    }

    private fun showDeleteConfirmDialog(note: Note) {
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.hapus_title)
            .setMessage(R.string.hapus_message)
            .setPositiveButton(R.string.hapus) { _, _ ->
                (requireActivity().applicationContext as KampusCareApp).repository?.deleteNote(note.id)
                requireActivity().finish()
            }
            .setNegativeButton(R.string.batal, null)
            .create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
