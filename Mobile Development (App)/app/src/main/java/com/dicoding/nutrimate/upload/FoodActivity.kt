package com.dicoding.nutrimate.upload

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.adapter.FoodAdapter
import com.dicoding.nutrimate.response.MealsItem

class FoodActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        recyclerView = findViewById(R.id.listfood)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val mealsData = intent.getParcelableArrayListExtra<MealsItem>("MEALS_DATA")

        if (mealsData != null) {
            foodAdapter = FoodAdapter(mealsData)
            recyclerView.adapter = foodAdapter
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        val btnCamera = findViewById<ImageView>(R.id.btncamera)
        btnCamera.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }
}


