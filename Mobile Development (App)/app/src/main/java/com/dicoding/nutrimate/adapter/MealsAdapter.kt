package com.dicoding.nutrimate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.response.MealsItemModel

class MealsAdapter(
    private val meals: List<MealsItemModel>,
    private val onItemClick: (MealsItemModel) -> Unit // Listener untuk item klik
) : RecyclerView.Adapter<MealsAdapter.MealsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return MealsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(meal)
    }

    override fun getItemCount(): Int = meals.size

    inner class MealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaResepTextView: TextView = itemView.findViewById(R.id.namaResep)
        private val imageView: ImageView = itemView.findViewById(R.id.imageFood)
        private val kaloriTextView: TextView = itemView.findViewById(R.id.calories)
        private val karboTextView: TextView = itemView.findViewById(R.id.karbo)
        private val proteinTextView: TextView = itemView.findViewById(R.id.protein)

        fun bind(meal: MealsItemModel) {
            namaResepTextView.text = meal.namaResep
            kaloriTextView.text = "${meal.calories}"
            karboTextView.text = "${meal.carbohydrates}"
            proteinTextView.text = "${meal.protein}"
            if (!meal.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(meal.imageUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_place_holder)
            }

            itemView.setOnClickListener {
                onItemClick(meal)
            }
        }
    }
}


