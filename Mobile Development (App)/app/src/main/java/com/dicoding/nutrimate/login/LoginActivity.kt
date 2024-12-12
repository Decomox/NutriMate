package com.dicoding.nutrimate.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutrimate.R
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dicoding.nutrimate.IsiActivity
import com.dicoding.nutrimate.api.ApiConfig2
import com.dicoding.nutrimate.data.LoginRequest
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.program.MainActivity
import com.dicoding.nutrimate.program.TipeActivity
import com.dicoding.nutrimate.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerbtn: Button
    private lateinit var userPreferences: UserPreferences
    private lateinit var loadinglogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userPreferences = UserPreferences(this)
        emailEditText = findViewById(R.id.email_login)
        passwordEditText = findViewById(R.id.password_login)
        loginButton = findViewById(R.id.login)
        registerbtn = findViewById(R.id.l_register)
        loadinglogin = findViewById(R.id.loadinglogin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loadinglogin.visibility = View.VISIBLE
                performLogin(email, password)
            } else {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        registerbtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig2.apiService.login(LoginRequest(email, password))
                withContext(Dispatchers.Main) {
                    loadinglogin.visibility = View.GONE
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            val token = it.data?.token
                            val user = it.data?.user
                            if (token != null && user != null) {
                                userPreferences.apply {
                                    saveProgram("cutting")
                                    saveUserData(token, user.name, user.id)
                                }

                                val mainIntent = Intent(this@LoginActivity, IsiActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(mainIntent)
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loadinglogin.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
