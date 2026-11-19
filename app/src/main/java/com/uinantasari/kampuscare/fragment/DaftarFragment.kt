package com.uinantasari.kampuscare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.uinantasari.kampuscare.KampusCareApp
import com.uinantasari.kampuscare.R
import com.uinantasari.kampuscare.adapter.NoteAdapter
import com.uinantasari.kampuscare.databinding.FragmentDaftarBinding
import com.uinantasari.kampuscare.model.Note
import com.uinantasari.kampuscare.viewmodel.NoteViewModel

/**
 * Fragment Daftar menampilkan daftar catatan fasilitas kampus dalam RecyclerView.
 * Mendukung filter, pengurutan terbaru/terlama, dan detail melalui klik item.
 */
class DaftarFragment : Fragment() {

    private var _binding: FragmentDaftarBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        noteViewModel = NoteViewModel(
            KampusCareApp.getInstance().repository
        )
        noteAdapter = NoteAdapter(emptyList()) { note ->
            // Klik item -> navigasi ke detail (RincianFragment)
            // Open RincianFragment activity
            openRincianActivity(note)
        } { note ->
            // Klik tombol hapus -> dialog konfirmasi
            showDeleteConfirmDialog(note)
        }

        binding.rvDaftar.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDaftar.adapter = noteAdapter

        // Setup FloatingActionButton
        binding.fabTambah.setOnClickListener {
            openFormActivity()
        }

        // Load catatan
        noteViewModel.getNotes()
        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.updateNotes(notes)
            binding.tvJumlah.text = getString(R.string.jumlah_catatan, notes.size)
        }
    }

    private fun openRincianActivity(note: Note) {
        val intent = Intent(requireContext(), com.uinantasari.kampuscare.activity.RincianActivity::class.java)
        intent.putExtra("NOTE_ID", note.id)
        startActivity(intent)
    }

    private fun showDeleteConfirmDialog(note: Note) {
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.hapus_title)
            .setMessage(R.string.hapus_message)
            .setPositiveButton(R.string.hapus) { _, _ ->
                noteViewModel.deleteNote(note.id)
            }
            .setNegativeButton(R.string.batal, null)
            .create()
        dialog.show()
    }

    private fun openFormActivity() {
        val intent = Intent(requireContext(), com.uinantasari.kampuscare.activity.FormActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
