package com.uinantasari.kampuscare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uinantasari.kampuscare.R
import com.uinantasari.kampuscare.databinding.FragmentBerandaBinding

/**
 * Fragment Beranda menampilkan:
 * - Nama aplikasi (KampusCare)
 * - Penjelasan singkat
 * - Jumlah catatan dari database
 * - Tombol "Tambah Catatan" untuk navigasi ke FormActivity
 */
class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol Tambah Catatan
        binding.btnTambahCatatan.setOnClickListener {
            val intent = Intent(requireContext(), FormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Update jumlah catatan setiap kali fragment muncul
        (requireActivity().applicationContext as com.uinantasari.kampuscare.KampusCareApp)
            .noteRepository?.let { repo ->
                repo.notes.value?.let { notes ->
                    binding.tvJumlahCatatan.text = notes.size.toString()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
