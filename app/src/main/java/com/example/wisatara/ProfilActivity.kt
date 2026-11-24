package com.example.wisatara

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatara.databinding.ActivityProfilBinding
import com.example.wisatara.repository.PesananRepository

class ProfilActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "wisatara_prefs"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_ROLE = "user_role"
        const val KEY_LANGUAGE = "app_language"

        fun canManageCrud(context: android.content.Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val role = prefs.getString(KEY_USER_ROLE, "Wisatawan") ?: "Wisatawan"
            return role.equals("Admin", ignoreCase = true) ||
                role.equals("Pemandu", ignoreCase = true)
        }
    }

    private lateinit var binding: ActivityProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserProfile()
        setupStats()
        setupMenuClickListeners()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
        setupStats()
    }

    private fun loadUserProfile() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedName = prefs.getString(KEY_USER_NAME, null)
        val savedEmail = prefs.getString(KEY_USER_EMAIL, null)
        val savedRole = prefs.getString(KEY_USER_ROLE, "Wisatawan") ?: "Wisatawan"

        if (!savedName.isNullOrBlank()) {
            binding.tvNamaWisatawan.text = savedName
        }
        if (!savedEmail.isNullOrBlank()) {
            binding.tvEmailWisatawan.text = savedEmail
        }

        binding.tvRoleBadge.text = when (savedRole) {
            "Admin" -> "🛡️ Administrator • Akses Penuh"
            "Pemandu" -> "🧭 Pemandu Lokal Verified • Anggota 2024"
            else -> "🛡️ Wisatawan Verified • Anggota 2024"
        }
    }

    private fun setupStats() {
        val completedCount = PesananRepository.getJumlahSelesai()
        binding.tvStatTurSelesai.text = completedCount.toString()
    }

    private fun setupMenuClickListeners() {
        binding.ivProfilAvatar.setOnClickListener {
            showEditProfileDialog()
        }

        binding.menuEditProfil.setOnClickListener {
            showEditProfileDialog()
        }

        binding.menuBahasa.setOnClickListener {
            showLanguageDialog()
        }

        binding.menuKeamanan.setOnClickListener {
            showHelpAndSecurityDialog()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showEditProfileDialog() {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }
        val etNama = EditText(this).apply {
            hint = "Nama Lengkap"
            setText(binding.tvNamaWisatawan.text.toString())
        }
        val etEmail = EditText(this).apply {
            hint = "Email"
            setText(binding.tvEmailWisatawan.text.toString())
        }
        container.addView(etNama)
        container.addView(etEmail)

        AlertDialog.Builder(this)
            .setTitle("Ubah Profil")
            .setView(container)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = etNama.text.toString().trim()
                val newEmail = etEmail.text.toString().trim()
                if (newName.isEmpty() || newEmail.isEmpty()) {
                    Toast.makeText(this, "Nama dan email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                    .putString(KEY_USER_NAME, newName)
                    .putString(KEY_USER_EMAIL, newEmail)
                    .apply()
                binding.tvNamaWisatawan.text = newName
                binding.tvEmailWisatawan.text = newEmail
                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showLanguageDialog() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val languages = arrayOf("Indonesia (ID)", "English (EN)")
        val current = prefs.getString(KEY_LANGUAGE, languages[0])
        var selectedIndex = languages.indexOf(current).coerceAtLeast(0)

        AlertDialog.Builder(this)
            .setTitle("Pilih Bahasa")
            .setSingleChoiceItems(languages, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton("Terapkan") { _, _ ->
                val chosen = languages[selectedIndex]
                prefs.edit().putString(KEY_LANGUAGE, chosen).apply()
                Toast.makeText(this, "Bahasa dipilih: $chosen", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showHelpAndSecurityDialog() {
        AlertDialog.Builder(this)
            .setTitle("Keamanan & Pusat Bantuan")
            .setMessage(
                "• Akun terverifikasi & dilindungi enkripsi.\n" +
                    "• Pusat Bantuan Wisatara aktif 24/7.\n" +
                    "• Hubungi support@wisatara.id jika butuh bantuan pemesanan.\n" +
                    "• Versi Aplikasi: 1.0"
            )
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Keluar Akun")
            .setMessage("Apakah Anda yakin ingin keluar dari akun Wisatara?")
            .setPositiveButton("Keluar") { dialog, _ ->
                Toast.makeText(this, "Berhasil keluar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
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
            startActivity(Intent(this, PesananActivity::class.java))
            finish()
        }

        binding.tabProfil.setOnClickListener {
            // Already in Profil
        }
    }
}
