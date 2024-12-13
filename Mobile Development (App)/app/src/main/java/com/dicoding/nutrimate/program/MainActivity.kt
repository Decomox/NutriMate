package com.dicoding.nutrimate.program

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.dicoding.nutrimate.R
import com.dicoding.nutrimate.data.UserPreferences
import com.dicoding.nutrimate.fragment.HomeFragment
import com.dicoding.nutrimate.response.MealsItem
import com.dicoding.nutrimate.response.WorkoutsItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPreferences = UserPreferences(this)

        val mealsData = intent.getParcelableArrayListExtra<MealsItem>("MEALS_DATA")
        val workoutsData = intent.getParcelableArrayListExtra<WorkoutsItem>("WORKOUTS_DATA")

        if (mealsData == null || workoutsData == null) {
            val savedMeals = userPreferences.getMealsData()
            val savedWorkouts = userPreferences.getWorkoutsData()

            if (savedMeals != null && savedWorkouts != null) {
                loadHomeFragment(savedMeals, savedWorkouts)
            } else {
                Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        } else {
            loadHomeFragment(mealsData, workoutsData)
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    private fun loadHomeFragment(mealsData: List<MealsItem>, workoutsData: List<WorkoutsItem>) {
        val homeFragment = HomeFragment()
        val bundle = Bundle().apply {
            putParcelableArrayList("meals", ArrayList(mealsData))
            putParcelableArrayList("workouts", ArrayList(workoutsData))
        }
        homeFragment.arguments = bundle

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        navController.navigate(R.id.homeFragment, bundle)
    }
}



