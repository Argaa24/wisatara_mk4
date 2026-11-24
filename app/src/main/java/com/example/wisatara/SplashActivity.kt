package com.example.wisatara

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatara.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var isNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMulai.setOnClickListener {
            navigateToLogin()
        }

        // Auto navigate after 3 seconds if user didn't click
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToLogin()
        }, 3000)
    }

    private fun navigateToLogin() {
        if (!isNavigated) {
            isNavigated = true
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
