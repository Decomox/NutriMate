package com.dicoding.nutrimate.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutrimate.R
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.dicoding.nutrimate.api.ApiConfig2
import com.dicoding.nutrimate.data.RegisterRequest
import com.dicoding.nutrimate.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var userPreferences: UserPreferences
    private lateinit var loadingregis: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userPreferences = UserPreferences(this)

        nameEditText = findViewById(R.id.username_register)
        emailEditText = findViewById(R.id.email_register)
        passwordEditText = findViewById(R.id.password_register)
        registerButton = findViewById(R.id.button_register)
        loadingregis = findViewById(R.id.loadingregister)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                loadingregis.visibility = View.VISIBLE
                performRegister(name, email, password)
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performRegister(name: String, email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig2.apiService.register(RegisterRequest(name, email, password))
                withContext(Dispatchers.Main) {
                    loadingregis.visibility = View.GONE
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        registerResponse?.let {
                            Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                            userPreferences.saveUserData(it.data?.email.toString(), it.data?.name, it.data?.id)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            finish()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loadingregis.visibility = View.GONE
                    Toast.makeText(this@RegisterActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
