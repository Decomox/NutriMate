package com.dicoding.nutrimate.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.program.DetailActivity
import com.dicoding.nutrimate.response.MealsItem

class FoodAdapter(private val mealsList: List<MealsItem>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val meal = mealsList[position]

        holder.namaResepTextView.text = meal.namaResep
        Glide.with(holder.itemView.context)
            .load(meal.imageUrl)
            .into(holder.imageView)

        holder.kalori.text = meal.calories
        holder.karbo.text = meal.carbohydrates
        holder.protein.text = meal.protein

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("MEAL_DETAIL", meal)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mealsList.size

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaResepTextView: TextView = itemView.findViewById(R.id.namaResep)
        val imageView: ImageView = itemView.findViewById(R.id.imageFood)
        val kalori: TextView = itemView.findViewById(R.id.calories)
        val karbo: TextView = itemView.findViewById(R.id.karbo)
        val protein: TextView = itemView.findViewById(R.id.protein)
    }
}
