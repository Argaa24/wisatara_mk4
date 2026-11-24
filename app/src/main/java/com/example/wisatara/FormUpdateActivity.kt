package com.example.wisatara

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatara.databinding.ActivityFormUpdateBinding
import com.example.wisatara.model.PaketTurModel
import com.example.wisatara.repository.WisataraRepository

class FormUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormUpdateBinding
    private var itemPaket: PaketTurModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemPaket = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_PAKET", PaketTurModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_PAKET")
        }

        if (itemPaket == null) {
            Toast.makeText(this, "Data tur tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        populateFields(itemPaket!!)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnBatal.setOnClickListener {
            finish()
        }

        binding.btnUpdateData.setOnClickListener {
            updateData()
        }
    }

    private fun populateFields(item: PaketTurModel) {
        binding.tvIdPaketTag.text = "ID: ${item.idPaket} • Status: Aktif"
        binding.etJudulTur.setText(item.judulTur)
        binding.etNamaPemandu.setText(item.namaPemandu)
        binding.etKotaDestinasi.setText(item.kotaDestinasi)
        binding.etKategoriTur.setText(item.kategoriTur)
        binding.etBahasaPemandu.setText(item.bahasaPemandu)
        binding.etDurasiJam.setText(item.durasiJam.toString())
        binding.etKuotaMaksimal.setText(item.kuotaMaksimal.toString())
        binding.etHargaPerOrang.setText(item.hargaPerOrang.toString())
        binding.etTitikKumpul.setText(item.titikKumpul)
        binding.etDeskripsiTur.setText(item.deskripsiTur)
    }

    private fun updateData() {
        val currentItem = itemPaket ?: return

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

        currentItem.judulTur = judul
        currentItem.namaPemandu = pemandu
        currentItem.kotaDestinasi = kota
        currentItem.kategoriTur = kategori
        currentItem.bahasaPemandu = bahasa
        currentItem.durasiJam = durasiStr.toIntOrNull() ?: currentItem.durasiJam
        currentItem.kuotaMaksimal = kuotaStr.toIntOrNull() ?: currentItem.kuotaMaksimal
        currentItem.hargaPerOrang = hargaStr.toIntOrNull() ?: currentItem.hargaPerOrang
        currentItem.titikKumpul = titik
        currentItem.deskripsiTur = deskripsi
        currentItem.highlightTag = "Maks. ${currentItem.kuotaMaksimal} Orang"

        WisataraRepository.update(currentItem)
        Toast.makeText(this, "Data tur berhasil diperbarui!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
