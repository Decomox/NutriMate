package com.dicoding.nutrimate

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutrimate.api.ApiConfig2
import com.dicoding.nutrimate.data.IsiRequest
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.program.TipeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IsiActivity : AppCompatActivity() {

    private lateinit var beratBadanEditText: EditText
    private lateinit var tinggiBadanEditText: EditText
    private lateinit var umurEditText: EditText
    private lateinit var kelaminAutoCompleteTextView: AutoCompleteTextView
    private lateinit var activityLevelAutoCompleteTextView: AutoCompleteTextView
    private lateinit var simpanButton: Button
    private lateinit var loading: ProgressBar
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi)
        beratBadanEditText = findViewById(R.id.beratbadan_isi)
        tinggiBadanEditText = findViewById(R.id.tinggibadan_isi)
        umurEditText = findViewById(R.id.umur_isi)
        kelaminAutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        activityLevelAutoCompleteTextView = findViewById(R.id.autoCompleteTextView2)
        simpanButton = findViewById(R.id.button3)
        loading = findViewById(R.id.loadingisi)
        userPreferences = UserPreferences(this)

        val kelaminOptions = listOf("male", "female")
        val kelaminAdapter = ArrayAdapter(this, R.layout.drop_down, kelaminOptions)
        kelaminAutoCompleteTextView.setAdapter(kelaminAdapter)

        val activityOptions = listOf("ringan", "berat", "sedang")
        val activityAdapter = ArrayAdapter(this, R.layout.drop_down, activityOptions)
        activityLevelAutoCompleteTextView.setAdapter(activityAdapter)

        simpanButton.setOnClickListener {
            val beratBadan = beratBadanEditText.text.toString()
            val tinggiBadan = tinggiBadanEditText.text.toString()
            val umur = umurEditText.text.toString()
            val kelamin = kelaminAutoCompleteTextView.text.toString()
            val activityLevel = activityLevelAutoCompleteTextView.text.toString()

            if (beratBadan.isNotEmpty() && tinggiBadan.isNotEmpty() && umur.isNotEmpty() && kelamin.isNotEmpty() && activityLevel.isNotEmpty()) {
                // Menampilkan progress bar
                loading.visibility = View.VISIBLE
                // Kirim data ke API
                sendDataToApi(beratBadan, tinggiBadan, umur, kelamin, activityLevel)
            } else {
                Toast.makeText(this, "Tolong isi semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendDataToApi(
        beratBadan: String,
        tinggiBadan: String,
        umur: String,
        kelamin: String,
        activityLevel: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Konversi data input ke format yang benar
                val request = IsiRequest(
                    weight = beratBadan.toInt(),
                    height = tinggiBadan.toInt(),
                    gender = if (kelamin == "Laki-laki") "male" else "female",
                    age = umur.toInt(),
                    activityLevel = activityLevel.lowercase() // pastikan konsistensi format
                )

                // Panggil API
                val response = ApiConfig2.apiService.rekomendasiProgram(request)

                withContext(Dispatchers.Main) {
                    loading.visibility = View.GONE

                    if (response.isSuccessful) {
                        val rekomResponse = response.body()
                        rekomResponse?.let {
                            val status = it.data?.status
                            val program = it.data?.program

                            // Simpan data status dan program di UserPreferences
                            userPreferences.saveRekomendasiProgram(program, status)

                            // Tampilkan pesan sukses dan alihkan ke TipeActivity
                            Toast.makeText(this@IsiActivity, "Data berhasil disimpan", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@IsiActivity, TipeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // Tampilkan pesan error jika respons tidak sukses
                        Toast.makeText(this@IsiActivity, "Terjadi kesalahan: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Tangani error jika terjadi
                withContext(Dispatchers.Main) {
                    loading.visibility = View.GONE
                    Toast.makeText(this@IsiActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
