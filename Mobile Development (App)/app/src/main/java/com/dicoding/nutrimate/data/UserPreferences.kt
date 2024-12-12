package com.dicoding.nutrimate.data

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.nutrimate.response.MealsItem
import com.dicoding.nutrimate.response.WorkoutsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveRekomendasiProgram(program: String?, status: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("program", program)
        editor.putString("status", status)
        editor.apply()
    }

    fun getProgram(): String? {
        return sharedPreferences.getString("program", "Tidak Ada Program")
    }

    fun getStatus(): String? {
        return sharedPreferences.getString("status", "Tidak Ada Status")
    }

    fun saveHeightWeightStatus(height: String, weight: String, status: String) {
        editor.putString("user_height", height)
        editor.putString("user_weight", weight)
        editor.putString("user_status", status)
        editor.apply()
    }
    fun getHeightWeightStatus(): Triple<String?, String?, String?> {
        val height = sharedPreferences.getString("user_height", null)
        val weight = sharedPreferences.getString("user_weight", null)
        val status = sharedPreferences.getString("user_status", null)
        return Triple(height, weight, status)
    }
    fun saveUserData(token: String, name: String?, userId: String?) {
        editor.putString("user_token", token)
        editor.putString("user_name", name)
        editor.putString("user_id", userId)
        editor.apply()
    }
    fun getUserData(): UserData? {
        val token = sharedPreferences.getString("user_token", null)
        val name = sharedPreferences.getString("user_name", null)
        val userId = sharedPreferences.getString("user_id", null)

        return if (token != null && name != null && userId != null) {
            UserData(token, name, userId)
        } else {
            null
        }
    }
    data class UserData(val token: String, val name: String, val userId: String)

    fun clearUserData() {
        editor.remove("user_name")
        editor.remove("user_email")
        editor.remove("user_id")
        editor.remove("user_token")
        editor.remove("user_height")
        editor.remove("user_weight")
        editor.remove("user_status")
        editor.apply()
    }

    fun saveMealsData(mealsData: List<MealsItem?>?) {
        val gson = Gson()
        val mealsJson = gson.toJson(mealsData)
        editor.putString("MEALS_DATA", mealsJson)
        editor.apply()
    }

    fun getMealsData(): List<MealsItem>? {
        val mealsJson = sharedPreferences.getString("MEALS_DATA", null)
        return if (mealsJson != null) {
            val gson = Gson()
            val mealsType = object : TypeToken<List<MealsItem>>() {}.type
            gson.fromJson(mealsJson, mealsType)
        } else {
            null
        }
    }

    fun saveWorkoutsData(workoutsData: List<WorkoutsItem?>?) {
        val gson = Gson()
        val workoutsJson = gson.toJson(workoutsData)
        editor.putString("WORKOUTS_DATA", workoutsJson)
        editor.apply()
    }

    fun getWorkoutsData(): List<WorkoutsItem>? {
        val workoutsJson = sharedPreferences.getString("WORKOUTS_DATA", null)
        return if (workoutsJson != null) {
            val gson = Gson()
            val workoutsType = object : TypeToken<List<WorkoutsItem>>() {}.type
            gson.fromJson(workoutsJson, workoutsType)
        } else {
            null
        }
    }

    fun saveCheckBoxStatus(position: Int, isChecked: Boolean) {
        editor.putBoolean("checkbox_$position", isChecked)
        editor.apply()
    }

    fun getCheckBoxStatus(position: Int): Boolean {
        return sharedPreferences.getBoolean("checkbox_$position", false)
    }


    fun saveProgram(program: String) {
        editor.putString("selected_program", program)
        editor.apply()
    }

    fun clearProgramData() {
        editor.remove("selected_program")
        editor.remove("MEALS_DATA")
        editor.remove("WORKOUTS_DATA")
        editor.apply()
    }
}

