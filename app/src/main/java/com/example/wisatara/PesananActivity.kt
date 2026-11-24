package com.example.wisatara

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wisatara.adapter.PesananAdapter
import com.example.wisatara.databinding.ActivityPesananBinding
import com.example.wisatara.model.PesananModel
import com.example.wisatara.repository.PesananRepository

class PesananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPesananBinding
    private lateinit var adapter: PesananAdapter
    private var currentFilterStatus: String = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFilterChips()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun setupRecyclerView() {
        adapter = PesananAdapter(
            listPesanan = mutableListOf(),
            onItemClick = { pesanan ->
                openPesananDetail(pesanan)
            },
            onCancelClick = { pesanan ->
                showCancelConfirmationDialog(pesanan)
            },
            onContactClick = { pesanan ->
                showContactHostDialog(pesanan)
            }
        )

        binding.rvPesanan.layoutManager = LinearLayoutManager(this)
        binding.rvPesanan.adapter = adapter
    }

    private fun openPesananDetail(pesanan: PesananModel) {
        val paket = com.example.wisatara.repository.WisataraRepository.getById(pesanan.idPaket)
        if (paket != null) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("EXTRA_PAKET", paket)
            startActivity(intent)
        } else {
            AlertDialog.Builder(this)
                .setTitle("Detail Pesanan ${pesanan.idPesanan}")
                .setMessage(
                    "Tur: ${pesanan.judulTur}\n" +
                        "Pemandu: ${pesanan.namaPemandu}\n" +
                        "Kota: ${pesanan.kotaDestinasi}\n" +
                        "Tanggal: ${pesanan.tanggalTur} (${pesanan.jumlahTamu} tamu)\n" +
                        "Titik Kumpul: ${pesanan.titikKumpul}\n" +
                        "Status: ${pesanan.statusPesanan}"
                )
                .setPositiveButton("Tutup", null)
                .show()
        }
    }

    private fun showContactHostDialog(pesanan: PesananModel) {
        val options = arrayOf(
            "Kirim Pesan ke ${pesanan.namaPemandu}",
            "Lihat Titik Kumpul (${pesanan.titikKumpul})"
        )
        AlertDialog.Builder(this)
            .setTitle("Hubungi Pemandu Lokal")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> Toast.makeText(
                        this,
                        "Pesan pengingat dikirim ke ${pesanan.namaPemandu} untuk ${pesanan.idPesanan}",
                        Toast.LENGTH_LONG
                    ).show()
                    1 -> AlertDialog.Builder(this)
                        .setTitle("Titik Kumpul")
                        .setMessage("${pesanan.titikKumpul}\n\nPemandu: ${pesanan.namaPemandu} (${pesanan.kotaDestinasi})")
                        .setPositiveButton("Oke", null)
                        .show()
                }
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    private fun refreshList() {
        val filteredList = PesananRepository.getByStatus(currentFilterStatus)
        adapter.updateData(filteredList)

        binding.tvCountPesanan.text = "• Menampilkan ${filteredList.size} Pesanan ($currentFilterStatus)"

        if (filteredList.isEmpty()) {
            binding.layoutEmptyPesanan.visibility = View.VISIBLE
            binding.rvPesanan.visibility = View.GONE
        } else {
            binding.layoutEmptyPesanan.visibility = View.GONE
            binding.rvPesanan.visibility = View.VISIBLE
        }
    }

    private fun setupFilterChips() {
        binding.chipFilterSemua.setOnClickListener {
            setActiveFilter("Semua")
        }
        binding.chipFilterAktif.setOnClickListener {
            setActiveFilter("Aktif")
        }
        binding.chipFilterSelesai.setOnClickListener {
            setActiveFilter("Selesai")
        }
        binding.chipFilterDibatalkan.setOnClickListener {
            setActiveFilter("Dibatalkan")
        }
    }

    private fun setActiveFilter(status: String) {
        currentFilterStatus = status

        val activeBg = ContextCompat.getDrawable(this, R.drawable.bg_pill_mint)
        val inactiveBg = ContextCompat.getDrawable(this, R.drawable.bg_pill_dark_green)
        val activeTextColor = ContextCompat.getColor(this, R.color.forest_green_dark)
        val inactiveTextColor = ContextCompat.getColor(this, R.color.white)

        // Reset all
        binding.chipFilterSemua.apply {
            background = if (status == "Semua") activeBg else inactiveBg
            setTextColor(if (status == "Semua") activeTextColor else inactiveTextColor)
            text = if (status == "Semua") "✓ Semua" else "Semua"
        }

        binding.chipFilterAktif.apply {
            background = if (status == "Aktif") activeBg else inactiveBg
            setTextColor(if (status == "Aktif") activeTextColor else inactiveTextColor)
            text = if (status == "Aktif") "✓ Aktif" else "Aktif"
        }

        binding.chipFilterSelesai.apply {
            background = if (status == "Selesai") activeBg else inactiveBg
            setTextColor(if (status == "Selesai") activeTextColor else inactiveTextColor)
            text = if (status == "Selesai") "✓ Selesai" else "Selesai"
        }

        binding.chipFilterDibatalkan.apply {
            background = if (status == "Dibatalkan") activeBg else inactiveBg
            setTextColor(if (status == "Dibatalkan") activeTextColor else inactiveTextColor)
            text = if (status == "Dibatalkan") "✓ Dibatalkan" else "Dibatalkan"
        }

        refreshList()
    }

    private fun showCancelConfirmationDialog(pesanan: PesananModel) {
        AlertDialog.Builder(this)
            .setTitle("Batalkan Pesanan")
            .setMessage("Apakah Anda yakin ingin membatalkan pesanan tur \"${pesanan.judulTur}\"?")
            .setPositiveButton("Ya, Batalkan") { dialog, _ ->
                PesananRepository.batalkanPesanan(pesanan.idPesanan)
                Toast.makeText(this, "Pesanan berhasil dibatalkan", Toast.LENGTH_SHORT).show()
                refreshList()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupBottomNavigation() {
        binding.tabBeranda.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        binding.tabKatalog.setOnClickListener {
            startActivity(Intent(this, ListDataActivity::class.java))
        }

        binding.tabPesanan.setOnClickListener {
            // Already in Pesanan
        }

        binding.tabProfil.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
            finish()
        }
    }
}
