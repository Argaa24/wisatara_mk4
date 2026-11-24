package com.example.wisatara

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatara.databinding.ActivityHomeBinding
import com.example.wisatara.repository.WisataraRepository

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Search Bar
        binding.searchBarHome.setOnClickListener {
            openKatalog(focusSearch = true)
        }

        // City Filter Chips
        binding.chipHomeSemua.setOnClickListener { openKatalog() }
        binding.chipHomeBali.setOnClickListener { openKatalog(query = "Bali") }
        binding.chipHomeJogja.setOnClickListener { openKatalog(query = "Yogyakarta") }
        binding.chipHomeJakarta.setOnClickListener { openKatalog(query = "Jakarta") }
        binding.chipHomeBandung.setOnClickListener { openKatalog(query = "Bandung") }

        // Local Host Cards
        binding.cardHostBudi.setOnClickListener { openHostOrCity("Budi", "Yogyakarta") }
        binding.cardHostWayan.setOnClickListener { openHostOrCity("Wayan", "Bali") }
        binding.cardHostSiti.setOnClickListener { openHostOrCity("Siti", "Jakarta") }
        binding.cardHostGede.setOnClickListener { openHostOrCity("Gede", "Bali") }

        // CRUD Entry actions
        binding.btnBukaKatalog.setOnClickListener {
            openKatalog()
        }

        binding.cardKelolaData.setOnClickListener {
            openKatalog()
        }

        binding.tvLihatSemuaGuide.setOnClickListener {
            openKatalog()
        }

        binding.ivAvatar.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }

        // Bottom Navigation
        binding.tabBeranda.setOnClickListener {
            binding.scrollViewHome.smoothScrollTo(0, 0)
        }

        binding.tabKatalog.setOnClickListener {
            openKatalog()
        }

        binding.tabPesanan.setOnClickListener {
            startActivity(Intent(this, PesananActivity::class.java))
        }

        binding.tabProfil.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }

        binding.cardPreview1.setOnClickListener {
            val previewPaket = WisataraRepository.getById("#WST-2024-001")
                ?: WisataraRepository.getAll().firstOrNull()
            if (previewPaket != null) {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("EXTRA_PAKET", previewPaket)
                startActivity(intent)
            } else {
                openKatalog()
            }
        }

        updateGreeting()
    }

    override fun onResume() {
        super.onResume()
        updateGreeting()
    }

    private fun updateGreeting() {
        val prefs = getSharedPreferences(ProfilActivity.PREFS_NAME, MODE_PRIVATE)
        val name = prefs.getString(ProfilActivity.KEY_USER_NAME, null)
        val role = prefs.getString(ProfilActivity.KEY_USER_ROLE, null)
        if (!name.isNullOrBlank()) {
            binding.tvHeroGreeting.text = if (!role.isNullOrBlank()) {
                "Halo, $name ($role)!"
            } else {
                "Halo, $name!"
            }
        }
    }

    private fun openHostOrCity(hostName: String, fallbackCity: String) {
        val paket = WisataraRepository.getAll().find {
            it.namaPemandu.contains(hostName, ignoreCase = true)
        }
        if (paket != null) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("EXTRA_PAKET", paket)
            startActivity(intent)
        } else {
            openKatalog(query = fallbackCity)
        }
    }

    private fun openKatalog(query: String? = null, focusSearch: Boolean = false) {
        val intent = Intent(this, ListDataActivity::class.java)
        if (!query.isNullOrEmpty()) {
            intent.putExtra(ListDataActivity.EXTRA_QUERY, query)
        }
        if (focusSearch) {
            intent.putExtra(ListDataActivity.EXTRA_FOCUS_SEARCH, true)
        }
        startActivity(intent)
    }
}
