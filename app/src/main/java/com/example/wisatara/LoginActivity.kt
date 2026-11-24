package com.example.wisatara

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatara.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    data class UserAccount(
        val email: String,
        val username: String,
        val password: String,
        val name: String,
        val role: String
    )

    companion object {
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_REMEMBERED_EMAIL = "remembered_email"
        const val KEY_REG_EMAIL = "registered_email"
        const val KEY_REG_PASS = "registered_password"
        const val KEY_REG_NAME = "registered_name"
        const val KEY_REG_ROLE = "registered_role"

        private val DEFAULT_ACCOUNTS = listOf(
            UserAccount(
                email = "admin@wisatara.com",
                username = "admin",
                password = "admin123",
                name = "Admin Wisatara",
                role = "Admin"
            ),
            UserAccount(
                email = "bagus@wisatara.com",
                username = "bagus",
                password = "password123",
                name = "Bagus Saputra",
                role = "Pemandu"
            ),
            UserAccount(
                email = "michael@gmail.com",
                username = "michael",
                password = "password123",
                name = "Michael",
                role = "Wisatawan"
            )
        )
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(ProfilActivity.PREFS_NAME, MODE_PRIVATE)
        if (prefs.getBoolean(KEY_REMEMBER_ME, false)) {
            binding.cbIngatSaya.isChecked = true
            binding.etEmail.setText(prefs.getString(KEY_REMEMBERED_EMAIL, ""))
        }

        // Quick-fill demo credentials
        binding.tvQuickAdmin.setOnClickListener {
            fillCredentials("admin@wisatara.com", "admin123")
        }
        binding.tvQuickPemandu.setOnClickListener {
            fillCredentials("bagus@wisatara.com", "password123")
        }
        binding.tvQuickWisatawan.setOnClickListener {
            fillCredentials("michael@gmail.com", "password123")
        }

        binding.btnLogin.setOnClickListener {
            val inputEmail = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (inputEmail.isEmpty()) {
                binding.etEmail.error = "Masukkan email atau username"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Masukkan kata sandi"
                return@setOnClickListener
            }

            val matchedAccount = authenticateUser(inputEmail, password)
            if (matchedAccount == null) {
                binding.etPassword.error = "Email atau kata sandi salah"
                Toast.makeText(
                    this,
                    "Email atau password tidak sesuai. Gunakan akun yang terdaftar.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putBoolean(KEY_REMEMBER_ME, binding.cbIngatSaya.isChecked)
                .putString(KEY_REMEMBERED_EMAIL, if (binding.cbIngatSaya.isChecked) matchedAccount.email else "")
                .putString(ProfilActivity.KEY_USER_NAME, matchedAccount.name)
                .putString(ProfilActivity.KEY_USER_EMAIL, matchedAccount.email)
                .putString(ProfilActivity.KEY_USER_ROLE, matchedAccount.role)
                .apply()

            Toast.makeText(
                this,
                "Login berhasil sebagai ${matchedAccount.role} (${matchedAccount.name})",
                Toast.LENGTH_SHORT
            ).show()

            // Proceed to Home
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.btnGoogleLogin.setOnClickListener {
            val googleUser = DEFAULT_ACCOUNTS.last() // Wisatawan Michael
            prefs.edit()
                .putString(ProfilActivity.KEY_USER_NAME, googleUser.name)
                .putString(ProfilActivity.KEY_USER_EMAIL, googleUser.email)
                .putString(ProfilActivity.KEY_USER_ROLE, googleUser.role)
                .apply()
            Toast.makeText(this, "Masuk dengan Google sebagai ${googleUser.name}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvLupaPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun fillCredentials(email: String, pass: String) {
        binding.etEmail.setText(email)
        binding.etPassword.setText(pass)
        binding.etEmail.error = null
        binding.etPassword.error = null
    }

    private fun authenticateUser(inputEmailOrUser: String, password: String): UserAccount? {
        val prefs = getSharedPreferences(ProfilActivity.PREFS_NAME, MODE_PRIVATE)
        val regEmail = prefs.getString(KEY_REG_EMAIL, null)
        val regPass = prefs.getString(KEY_REG_PASS, null)
        val regName = prefs.getString(KEY_REG_NAME, null)
        val regRole = prefs.getString(KEY_REG_ROLE, "Wisatawan") ?: "Wisatawan"

        val allAccounts = DEFAULT_ACCOUNTS.toMutableList()
        if (!regEmail.isNullOrBlank() && !regPass.isNullOrBlank() && !regName.isNullOrBlank()) {
            allAccounts.add(
                UserAccount(
                    email = regEmail,
                    username = regEmail.substringBefore("@"),
                    password = regPass,
                    name = regName,
                    role = regRole
                )
            )
        }

        return allAccounts.find { account ->
            (account.email.equals(inputEmailOrUser, ignoreCase = true) ||
                account.username.equals(inputEmailOrUser, ignoreCase = true)) &&
                account.password == password
        }
    }

    private fun showForgotPasswordDialog() {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }
        val etResetEmail = EditText(this).apply {
            hint = "Masukkan email terdaftar"
            setText(binding.etEmail.text.toString().trim())
        }
        container.addView(etResetEmail)

        AlertDialog.Builder(this)
            .setTitle("Reset Kata Sandi")
            .setMessage("Kami akan mengirimkan tautan pemulihan kata sandi ke email Anda.")
            .setView(container)
            .setPositiveButton("Kirim") { _, _ ->
                val email = etResetEmail.text.toString().trim()
                if (email.isEmpty()) {
                    Toast.makeText(this, "Masukkan alamat email terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tautan reset kata sandi dikirim ke $email", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
