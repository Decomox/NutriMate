package com.dicoding.nutrimate.program

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.api.ApiConfig1
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.response.MainResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TipeActivity : AppCompatActivity() {

    private lateinit var programTextView: TextView
    private lateinit var rekomendasiButton: Button
    private lateinit var userPreferences: UserPreferences
    private lateinit var loadingtipe: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipe)
        rekomendasiButton = findViewById(R.id.rekomendasi)
        userPreferences = UserPreferences(this)
        loadingtipe = findViewById(R.id.loadingtipe)
        programTextView = findViewById(R.id.tipe)

        val program = userPreferences.getProgram()
        val status = userPreferences.getStatus()
        if (status != null) {
            rekomendasiButton.text = program
        }

        programTextView.text = "$status"

        val menurunkanButton: Button = findViewById(R.id.menurunkan)
        val menaikkanButton: Button = findViewById(R.id.menaikkan)
        val mempertahanButton: Button = findViewById(R.id.pertahan)

        menurunkanButton.setOnClickListener {
            loadingtipe.visibility = View.VISIBLE
            saveProgramAndGetData("cutting")
        }

        menaikkanButton.setOnClickListener {
            loadingtipe.visibility = View.VISIBLE
            saveProgramAndGetData("bulking")
        }

        mempertahanButton.setOnClickListener {
            loadingtipe.visibility = View.VISIBLE
            saveProgramAndGetData("maintaining")
        }
    }

    private fun saveProgramAndGetData(type: String) {
        userPreferences.clearProgramData()
        userPreferences.saveProgram(type)

        getDataFromApi(type)
    }

    private fun getDataFromApi(type: String) {
        val call = when (type) {
            "cutting" -> ApiConfig1.apiService.getCuttingData()
            "bulking" -> ApiConfig1.apiService.getBulkingData()
            "maintaining" -> ApiConfig1.apiService.getMaintainingData()
            else -> throw IllegalArgumentException("Unknown type")
        }

        call.enqueue(object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                loadingtipe.visibility = View.GONE
                if (response.isSuccessful) {
                    val mealsData = response.body()?.meals
                    val workoutsData = response.body()?.workouts

                    mealsData?.let { userPreferences.saveMealsData(it) }
                    workoutsData?.let { userPreferences.saveWorkoutsData(it) }

                    val intent = Intent(this@TipeActivity, MainActivity::class.java)
                    intent.putParcelableArrayListExtra("MEALS_DATA", ArrayList(mealsData))
                    intent.putParcelableArrayListExtra("WORKOUTS_DATA", ArrayList(workoutsData))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("API_ERROR", "Error Response: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@TipeActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                loadingtipe.visibility = View.GONE
                Log.e("API_FAILURE", "Failure: ${t.message}")
                Toast.makeText(this@TipeActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}









