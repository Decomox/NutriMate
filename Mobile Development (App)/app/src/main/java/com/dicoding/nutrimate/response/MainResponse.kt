package com.dicoding.nutrimate.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MainResponse(

	@field:SerializedName("workouts")
	val workouts: List<WorkoutsItem?>? = null,

	@field:SerializedName("dietType")
	val dietType: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("meals")
	val meals: List<MealsItem?>? = null
) : Parcelable

@Parcelize
data class WorkoutsItem(

	@field:SerializedName("duration")
	val duration: Int? = null,

	@field:SerializedName("intensity")
	val intensity: String? = null,

	@field:SerializedName("equipment_needed")
	val equipmentNeeded: String? = null,

	@field:SerializedName("how_to_perform")
	val howToPerform: String? = null,

	@field:SerializedName("target_muscles")
	val targetMuscles: String? = null,

	@field:SerializedName("workout_name")
	val workoutName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("diet_type")
	val dietType: String? = null
) : Parcelable

@Parcelize
data class MealsItem(

	@field:SerializedName("carbohydrates")
	val carbohydrates: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("protein")
	val protein: String? = null,

	@field:SerializedName("fat")
	val fat: String? = null,

	@field:SerializedName("nama_resep")
	val namaResep: String? = null,

	@field:SerializedName("bahan-resep")
	val bahanResep: String? = null,

	@field:SerializedName("diet_type")
	val dietType: String? = null,

	@field:SerializedName("calories")
	val calories: String? = null,

	@field:SerializedName("steps")
	val steps: String? = null,

	@field:SerializedName("bahan_resep_pilihan")
	val bahanResepPilihan: String? = null,

	@field:SerializedName("url-masak")
	val urlMasak: String? = null
) : Parcelable
