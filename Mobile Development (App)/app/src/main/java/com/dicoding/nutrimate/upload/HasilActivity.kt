package com.dicoding.nutrimate.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.adapter.MealsAdapter
import com.dicoding.nutrimate.databinding.ActivityHasilBinding
import com.dicoding.nutrimate.program.HasilDetailActivity
import com.dicoding.nutrimate.response.MealsItemModel

class HasilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilBinding
    private lateinit var mealsRecyclerView: RecyclerView
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gambar = intent.getStringExtra("gambar")
        val result = intent.getStringExtra("result")
        val mealsList = intent.getParcelableArrayListExtra<MealsItemModel>("meals") ?: emptyList() // Mengambil daftar meals

        binding.resultTextView.text = result ?: "No result found"

        if (gambar != null) {
            val uri = Uri.parse(gambar)
            binding.gambarhasil.setImageURI(uri)
        } else {
            binding.gambarhasil.setImageResource(R.drawable.ic_place_holder)
        }


        mealsRecyclerView = binding.rvHasil
        mealsAdapter = MealsAdapter(mealsList) { meal ->

            val intent = Intent(this, HasilDetailActivity::class.java)
            intent.putExtra("meal", meal)
            startActivity(intent)
        }
        mealsRecyclerView.layoutManager = LinearLayoutManager(this)
        mealsRecyclerView.adapter = mealsAdapter
    }
}



