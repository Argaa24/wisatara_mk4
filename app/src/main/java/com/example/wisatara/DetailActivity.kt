package com.example.wisatara

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatara.databinding.ActivityDetailBinding
import com.example.wisatara.databinding.DialogConfirmDeleteBinding
import com.example.wisatara.model.PaketTurModel
import com.example.wisatara.model.PesananModel
import com.example.wisatara.repository.PesananRepository
import com.example.wisatara.repository.WisataraRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var itemPaket: PaketTurModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
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

        displayData(itemPaket!!)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            shareTour()
        }

        binding.btnPesanTur.setOnClickListener {
            showBookingDialog()
        }

        binding.btnUbahData.setOnClickListener {
            val intent = Intent(this, FormUpdateActivity::class.java)
            intent.putExtra("EXTRA_PAKET", itemPaket)
            startActivity(intent)
        }

        binding.btnHapusData.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data in case it was updated in FormUpdateActivity
        val currentId = itemPaket?.idPaket
        if (currentId != null) {
            val updated = WisataraRepository.getById(currentId)
            if (updated != null) {
                itemPaket = updated
                displayData(updated)
            } else {
                finish()
            }
        }
    }

    private fun displayData(item: PaketTurModel) {
        val formatRp = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedHarga = formatRp.format(item.hargaPerOrang).replace(",00", "")

        binding.tvJudulTur.text = item.judulTur
        binding.tvHargaPerOrang.text = "$formattedHarga / orang"
        binding.tvRatingBadge.text = "★ ${item.rating} (${item.jumlahUlasan} ulasan)"
        binding.chipDurasi.text = "🕒 ${item.durasiJam} Jam Durasi"
        binding.chipKategori.text = "🍴 ${item.kategoriTur}"
        binding.chipPrivate.text = "👥 ${item.highlightTag}"
        binding.chipKota.text = "📍 ${item.kotaDestinasi}"

        binding.tvNamaPemandu.text = item.namaPemandu
        binding.tvKotaPemandu.text = "Local Host di ${item.kotaDestinasi}"
        binding.tvTaglinePemandu.text = "\"${item.taglinePemandu}\""
        binding.tvTitikKumpul.text = item.titikKumpul
        binding.tvDeskripsiTur.text = item.deskripsiTur
    }

    private fun showDeleteConfirmationDialog() {
        val dialogBinding = DialogConfirmDeleteBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.tvDeleteMessage.text =
            "Apakah Anda yakin ingin menghapus data paket tur \"${itemPaket?.judulTur}\"? Tindakan ini tidak dapat dibatalkan."

        dialogBinding.btnBatalDelete.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnConfirmDelete.setOnClickListener {
            val id = itemPaket?.idPaket
            if (id != null) {
                WisataraRepository.deleteById(id)
                Toast.makeText(this, "Paket tur berhasil dihapus!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                finish()
            }
        }

        dialog.show()
    }

    private fun shareTour() {
        val item = itemPaket ?: return
        val formatRp = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedHarga = formatRp.format(item.hargaPerOrang).replace(",00", "")
        val shareText = "Yuk ikut tur \"${item.judulTur}\" di ${item.kotaDestinasi} bersama ${item.namaPemandu}!\n" +
            "Harga: $formattedHarga / orang • Durasi: ${item.durasiJam} Jam\n" +
            "Temukan di aplikasi Wisatara."

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Bagikan Paket Tur"))
    }

    private fun showBookingDialog() {
        val item = itemPaket ?: return
        val formatRp = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 3) }
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

        var guestCount = 2.coerceAtMost(item.kuotaMaksimal.coerceAtLeast(1))
        var selectedDate = dateFormat.format(calendar.time)

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }

        val tvDate = TextView(this).apply {
            text = "Tanggal Tur: $selectedDate (Ketuk untuk ubah)"
            textSize = 14f
            setPadding(0, 8, 0, 16)
        }

        val tvGuests = TextView(this).apply {
            textSize = 14f
            setPadding(0, 8, 0, 8)
        }

        val tvTotal = TextView(this).apply {
            textSize = 15f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 8, 0, 16)
        }

        fun refreshSummary() {
            tvGuests.text = "Jumlah Tamu: $guestCount orang (Maks. ${item.kuotaMaksimal})"
            val totalFormatted = formatRp.format(item.hargaPerOrang * guestCount).replace(",00", "")
            tvTotal.text = "Total Pembayaran: $totalFormatted"
        }
        refreshSummary()

        tvDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = dateFormat.format(calendar.time)
                    tvDate.text = "Tanggal Tur: $selectedDate (Ketuk untuk ubah)"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val btnRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 8, 0, 8)
        }
        val btnMinus = android.widget.Button(this).apply {
            text = "−"
            setOnClickListener {
                if (guestCount > 1) {
                    guestCount--
                    refreshSummary()
                }
            }
        }
        val btnPlus = android.widget.Button(this).apply {
            text = "+"
            setOnClickListener {
                if (guestCount < item.kuotaMaksimal) {
                    guestCount++
                    refreshSummary()
                }
            }
        }
        btnRow.addView(btnMinus)
        btnRow.addView(btnPlus)

        container.addView(tvDate)
        container.addView(tvGuests)
        container.addView(btnRow)
        container.addView(tvTotal)

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Pesanan")
            .setView(container)
            .setPositiveButton("Pesan Sekarang") { _, _ ->
                createBooking(item, selectedDate, guestCount)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun createBooking(item: PaketTurModel, tanggalTur: String, jumlahTamu: Int) {
        val randomBookingNum = (100..999).random()
        val newBookingId = "#BOOK-2024-$randomBookingNum"

        val newPesanan = PesananModel(
            idPesanan = newBookingId,
            idPaket = item.idPaket,
            judulTur = item.judulTur,
            namaPemandu = item.namaPemandu,
            kotaDestinasi = item.kotaDestinasi,
            tanggalTur = tanggalTur,
            jumlahTamu = jumlahTamu,
            totalHarga = item.hargaPerOrang * jumlahTamu,
            statusPesanan = "Aktif",
            titikKumpul = item.titikKumpul
        )

        PesananRepository.addPesanan(newPesanan)
        Toast.makeText(this, "Pesanan tur berhasil dibuat!", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, PesananActivity::class.java))
    }
}
