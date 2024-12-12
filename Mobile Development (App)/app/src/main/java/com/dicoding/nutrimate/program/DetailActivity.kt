package com.dicoding.nutrimate.program

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.response.MealsItem

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val mealDetail: MealsItem? = intent.getParcelableExtra("MEAL_DETAIL")

        mealDetail?.let { meal ->
            val namaResepTextView: TextView = findViewById(R.id.tv_item_name)
            val imageView: ImageView = findViewById(R.id.imageurl)
            val bahanResepTextView: TextView = findViewById(R.id.bahanResep)
            val caloriesTextView: TextView = findViewById(R.id.calories)
            val proteinTextView: TextView = findViewById(R.id.protein)
            val carbohydratesTextView: TextView = findViewById(R.id.karbo)
            val stepsTextView: TextView = findViewById(R.id.steps)

            namaResepTextView.text = meal.namaResep
            Glide.with(this)
                .load(meal.imageUrl)
                .into(imageView)
            bahanResepTextView.text = meal.bahanResep
            caloriesTextView.text = meal.calories
            proteinTextView.text = meal.protein
            carbohydratesTextView.text = meal.carbohydrates
            stepsTextView.text = meal.steps

            val urlMasak = meal.urlMasak
            if (!urlMasak.isNullOrEmpty()) {
                val buttonCook: Button = findViewById(R.id.buttonCook)
                buttonCook.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlMasak))
                    startActivity(intent)
                }
            }
        }
    }
}
