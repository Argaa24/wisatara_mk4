package com.example.wisatara

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatara.databinding.ActivityFormInputBinding
import com.example.wisatara.model.PaketTurModel
import com.example.wisatara.repository.WisataraRepository

class FormInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnReset.setOnClickListener {
            resetForm()
        }

        binding.btnSimpanData.setOnClickListener {
            simpanData()
        }
    }

    private fun resetForm() {
        binding.etJudulTur.text?.clear()
        binding.etNamaPemandu.text?.clear()
        binding.etKotaDestinasi.text?.clear()
        binding.etKategoriTur.text?.clear()
        binding.etBahasaPemandu.text?.clear()
        binding.etDurasiJam.text?.clear()
        binding.etKuotaMaksimal.text?.clear()
        binding.etHargaPerOrang.text?.clear()
        binding.etTitikKumpul.text?.clear()
        binding.etDeskripsiTur.text?.clear()
        Toast.makeText(this, "Form telah direset", Toast.LENGTH_SHORT).show()
    }

    private fun simpanData() {
        val judul = binding.etJudulTur.text.toString().trim()
        val pemandu = binding.etNamaPemandu.text.toString().trim()
        val kota = binding.etKotaDestinasi.text.toString().trim()
        val kategori = binding.etKategoriTur.text.toString().trim()
        val bahasa = binding.etBahasaPemandu.text.toString().trim()
        val durasiStr = binding.etDurasiJam.text.toString().trim()
        val kuotaStr = binding.etKuotaMaksimal.text.toString().trim()
        val hargaStr = binding.etHargaPerOrang.text.toString().trim()
        val titik = binding.etTitikKumpul.text.toString().trim()
        val deskripsi = binding.etDeskripsiTur.text.toString().trim()

        if (judul.isEmpty()) {
            binding.etJudulTur.error = "Wajib diisi"
            return
        }
        if (pemandu.isEmpty()) {
            binding.etNamaPemandu.error = "Wajib diisi"
            return
        }
        if (hargaStr.isEmpty()) {
            binding.etHargaPerOrang.error = "Wajib diisi"
            return
        }

        val randomId = "#WST-2024-${(100..999).random()}"
        val newItem = PaketTurModel(
            idPaket = randomId,
            judulTur = judul,
            namaPemandu = pemandu,
            namaPanggilan = pemandu,
            taglinePemandu = "Pemandu Berlisensi & Ramah",
            kotaDestinasi = if (kota.isNotEmpty()) kota else "Indonesia",
            kategoriTur = if (kategori.isNotEmpty()) kategori else "General Tour",
            bahasaPemandu = if (bahasa.isNotEmpty()) bahasa else "Bahasa Indonesia",
            durasiJam = durasiStr.toIntOrNull() ?: 3,
            kuotaMaksimal = kuotaStr.toIntOrNull() ?: 6,
            hargaPerOrang = hargaStr.toIntOrNull() ?: 350000,
            titikKumpul = if (titik.isNotEmpty()) titik else "Meeting Point Utama",
            deskripsiTur = if (deskripsi.isNotEmpty()) deskripsi else "Pengalaman tur otentik bersama pemandu lokal Wisatara.",
            rating = 5.0,
            jumlahUlasan = 1,
            highlightTag = "Maks. ${kuotaStr.ifEmpty { "6" }} Orang"
        )

        WisataraRepository.add(newItem)
        Toast.makeText(this, "Paket tur berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
