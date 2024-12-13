package com.dicoding.nutrimate.program

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.databinding.ActivityHasilDetailBinding
import com.dicoding.nutrimate.response.MealsItemModel

class HasilDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val meal = intent.getParcelableExtra<MealsItemModel>("meal")

        meal?.let {
            val urlMasak = it.urlMasak
            if (!urlMasak.isNullOrEmpty()) {
                binding.buttonCookh.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlMasak))
                    startActivity(intent)
                }
            } else {
                Log.d("HasilDetailActivity", "URL Masak tidak tersedia")
            }
            binding.fname.text = it.namaResep
            binding.caloriesh.text = "${it.calories}"
            binding.karboh.text = "${it.carbohydrates}"
            binding.proteinh.text = "${it.protein}"
            if (!it.imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(binding.imagehasil)
            }
            binding.bahanReseph.text = it.bahanResep
            binding.stepsh.text = it.steps

        }
    }
}

