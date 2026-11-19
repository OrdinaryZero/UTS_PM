package com.uinantasari.kampuscare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uinantasari.kampuscare.R
import com.uinantasari.kampuscare.databinding.FragmentPengaturanBinding

/**
 * Fragment Pengaturan menampilkan:
 * - Pilihan urutan daftar: Terbaru atau Terlama
 * - Identitas pengembang (nama, NIM, kelas)
 * - Informasi aplikasi
 *
 * Data preferensi disimpan menggunakan SharedPreferences.
 */
class PengaturanFragment : Fragment() {

    private var _binding: FragmentPengaturanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaturanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrderOptions()
        setupDeveloperInfo()
    }

    private fun setupOrderOptions() {
        val order = PreferenceManager.getInt(PreferenceManager.KEY_LIST_ORDER, 0)

        binding.rbTerbaru.isChecked = order == 0
        binding.rbTerlama.isChecked = order == 1

        binding.rbTerbaru.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                PreferenceManager.putInt(PreferenceManager.KEY_LIST_ORDER, 0)
            }
        }

        binding.rbTerlama.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                PreferenceManager.putInt(PreferenceManager.KEY_LIST_ORDER, 1)
            }
        }
    }

    private fun setupDeveloperInfo() {
        binding.tvNama.text = getString(R.string.dev_name)
        binding.tvNIM.text = getString(R.string.dev_nim)
        binding.tvKelas.text = getString(R.string.dev_kelas)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
