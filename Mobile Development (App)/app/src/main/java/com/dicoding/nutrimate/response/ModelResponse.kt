package com.dicoding.nutrimate.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ModelResponse(
	@field:SerializedName("data")
	val data: DataResponse? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class MealsItemModel(

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

):Parcelable

data class DataResponse(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("meals")
	val meals: List<MealsItemModel?>? = null
)


