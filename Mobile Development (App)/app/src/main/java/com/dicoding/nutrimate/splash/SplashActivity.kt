package com.dicoding.nutrimate.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.login.LoginActivity
import com.dicoding.nutrimate.program.MainActivity
import com.dicoding.nutrimate.program.TipeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userPreferences = UserPreferences(this)

        // Animasi logo splash screen
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim)
        findViewById<ImageView>(R.id.iv_logo).startAnimation(fadeIn)

        // Delay sebelum melanjutkan ke halaman berikutnya
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginAndNavigate()
        }, 2000)

        // Set splash screen fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun checkLoginAndNavigate() {
        val userData = userPreferences.getUserData() // Ambil data pengguna

        // Cek apakah token login ada (pengguna sudah login)
        val intent = if (userData != null) {
            // Jika pengguna sudah login, lanjut ke MainActivity
            Intent(this, MainActivity::class.java)
        } else {
            // Jika pengguna belum login, arahkan ke LoginActivity
            Intent(this, LoginActivity::class.java)
        }

        // Menggunakan flags agar aktivitas sebelumnya tidak ada dalam stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Menghentikan SplashActivity agar tidak bisa kembali ke sana
    }
}

