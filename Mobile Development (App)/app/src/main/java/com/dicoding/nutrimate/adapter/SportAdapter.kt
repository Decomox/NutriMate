package com.dicoding.nutrimate.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.program.DetailSportActivity
import com.dicoding.nutrimate.response.WorkoutsItem

class SportAdapter(
    private val sportsList: List<WorkoutsItem>,
    private val userPreferences: UserPreferences
) : RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sport, parent, false)
        return SportViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        val sport = sportsList[position]

        holder.namasport.text = sport.workoutName
        holder.intesitas.text = sport.intensity
        holder.target.text = sport.targetMuscles
        holder.durasi.text = sport.duration.toString()

        val isChecked = userPreferences.getCheckBoxStatus(position)
        holder.checkBox.isChecked = isChecked

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            userPreferences.saveCheckBoxStatus(position, isChecked)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailSportActivity::class.java).apply {
                putExtra("WORKOUTS_DETAIL", sport)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = sportsList.size

    class SportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namasport: TextView = itemView.findViewById(R.id.namaOlahraga)
        val intesitas: TextView = itemView.findViewById(R.id.intesitas)
        val target: TextView = itemView.findViewById(R.id.target)
        val durasi: TextView = itemView.findViewById(R.id.durasi)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxOlahraga)
    }
}

