package com.dicoding.nutrimate.program

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.adapter.SportAdapter
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.response.WorkoutsItem

class SportActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: SportAdapter
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)

        userPreferences = UserPreferences(this)

        recyclerView = findViewById(R.id.sports_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val workoutsData = intent.getParcelableArrayListExtra<WorkoutsItem>("WORKOUTS_DATA")

        if (workoutsData != null) {
            foodAdapter = SportAdapter(workoutsData, userPreferences)
            recyclerView.adapter = foodAdapter
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
}
