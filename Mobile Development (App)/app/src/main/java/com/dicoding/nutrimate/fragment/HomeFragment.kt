package com.dicoding.nutrimate.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.program.SportActivity
import com.dicoding.nutrimate.program.TipeActivity
import com.dicoding.nutrimate.response.MealsItem
import com.dicoding.nutrimate.response.WorkoutsItem
import com.dicoding.nutrimate.upload.FoodActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var mealsList: List<MealsItem>? = null
    private var workoutsList: List<WorkoutsItem>? = null
    private lateinit var dietTypeTextView: TextView
    private lateinit var userPreferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())

        // Mengambil data dari arguments (pindahan dari MainActivity) atau SharedPreferences jika tidak ada
        mealsList = arguments?.getParcelableArrayList("meals") ?: userPreferences.getMealsData()
        workoutsList = arguments?.getParcelableArrayList("workouts") ?: userPreferences.getWorkoutsData()

        dietTypeTextView = view.findViewById(R.id.tipetubuh)

        // Menampilkan jenis diet jika mealsList tersedia
        if (mealsList != null && mealsList!!.isNotEmpty()) {
            val dietType = mealsList!![0].dietType
            dietTypeTextView.text = dietType ?: "Diet Type Tidak Diketahui"
        }

        val breakfastImage: ImageView = view.findViewById(R.id.breakfast)
        val lunchImage: ImageView = view.findViewById(R.id.lunch)
        val dinnerImage: ImageView = view.findViewById(R.id.dinner)
        val olahragaImage: ImageView = view.findViewById(R.id.olahraga)

        breakfastImage.setOnClickListener {
            navigateToFoodActivity()
        }

        lunchImage.setOnClickListener {
            navigateToFoodActivity()
        }

        dinnerImage.setOnClickListener {
            navigateToFoodActivity()
        }

        olahragaImage.setOnClickListener {
            navigateToSportActivity()
        }

        val ubahModeButton: Button = view.findViewById(R.id.ubahModeButton)
        ubahModeButton.setOnClickListener {
            userPreferences.clearProgramData()
            navigateToTipeActivity()
        }
    }

    private fun navigateToFoodActivity() {
        val intent = Intent(requireContext(), FoodActivity::class.java)
        mealsList?.let {
            intent.putParcelableArrayListExtra("MEALS_DATA", ArrayList(it))
        }
        startActivity(intent)
    }

    private fun navigateToSportActivity() {
        val intent = Intent(requireContext(), SportActivity::class.java)
        workoutsList?.let {
            intent.putParcelableArrayListExtra("WORKOUTS_DATA", ArrayList(it))
        }
        startActivity(intent)
    }

    private fun navigateToTipeActivity() {
        val intent = Intent(requireContext(), TipeActivity::class.java)
        startActivity(intent)
    }
}





