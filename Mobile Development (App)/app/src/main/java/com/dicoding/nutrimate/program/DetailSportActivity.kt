package com.dicoding.nutrimate.program

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.response.WorkoutsItem

class DetailSportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sport)

        val sportsDetail: WorkoutsItem? = intent.getParcelableExtra("WORKOUTS_DETAIL")

        sportsDetail?.let { sport ->
            val nameSport: TextView = findViewById(R.id.tv_item_sport)
            val deskripsiTextView: TextView = findViewById(R.id.deskripsi)
            val intesitasTextView: TextView = findViewById(R.id.intesitas)
            val targetTextView: TextView = findViewById(R.id.target)
            val durasiTextView: TextView = findViewById(R.id.durasi)
            val alatTextView: TextView = findViewById(R.id.alat)
            val caraTextView: TextView = findViewById(R.id.cara)

            nameSport.text = sport.workoutName
            deskripsiTextView.text = sport.description
            intesitasTextView.text = sport.intensity
            targetTextView.text = sport.targetMuscles
            durasiTextView.text = sport.duration.toString()
            alatTextView.text = sport.equipmentNeeded
            caraTextView.text = sport.howToPerform
        }
    }
}