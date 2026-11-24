package com.example.wisatara

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.wisatara.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isWisatawanSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardWisatawan.setOnClickListener {
            selectWisatawan()
        }

        binding.cardPemandu.setOnClickListener {
            selectPemandu()
        }

        binding.btnRegister.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val confirmPass = binding.etKonfirmasiPassword.text.toString().trim()

            if (nama.isEmpty()) {
                binding.etNama.error = "Masukkan nama lengkap"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.etEmail.error = "Masukkan email"
                return@setOnClickListener
            }
            if (pass.isEmpty()) {
                binding.etPassword.error = "Masukkan password"
                return@setOnClickListener
            }
            if (pass != confirmPass) {
                binding.etKonfirmasiPassword.error = "Password tidak cocok"
                return@setOnClickListener
            }

            if (!binding.cbSyarat.isChecked) {
                Toast.makeText(this, "Setujui Syarat & Ketentuan terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val role = if (isWisatawanSelected) "Wisatawan" else "Pemandu"
            getSharedPreferences(ProfilActivity.PREFS_NAME, MODE_PRIVATE).edit()
                .putString(LoginActivity.KEY_REG_NAME, nama)
                .putString(LoginActivity.KEY_REG_EMAIL, email)
                .putString(LoginActivity.KEY_REG_PASS, pass)
                .putString(LoginActivity.KEY_REG_ROLE, role)
                .apply()

            Toast.makeText(this, "Registrasi $role berhasil! Silakan Login", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.tvToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun selectWisatawan() {
        isWisatawanSelected = true
        val emerald = ContextCompat.getColor(this, R.color.emerald_primary)
        val gray = ContextCompat.getColor(this, R.color.gray_border)

        binding.cardWisatawan.strokeColor = emerald
        binding.cardWisatawan.strokeWidth = 4
        binding.cardPemandu.strokeColor = gray
        binding.cardPemandu.strokeWidth = 2

        binding.tvRoleInfo.text = "Akses Wisatawan Eksklusif - Jelajahi tur otentik & budaya langsung bersama warga lokal"
    }

    private fun selectPemandu() {
        isWisatawanSelected = false
        val emerald = ContextCompat.getColor(this, R.color.emerald_primary)
        val gray = ContextCompat.getColor(this, R.color.gray_border)

        binding.cardWisatawan.strokeColor = gray
        binding.cardWisatawan.strokeWidth = 2
        binding.cardPemandu.strokeColor = emerald
        binding.cardPemandu.strokeWidth = 4

        binding.tvRoleInfo.text = "Mitra Local Host - Tawarkan pengalaman otentik daerah Anda & kelola jadwal tur privat"
    }
}
