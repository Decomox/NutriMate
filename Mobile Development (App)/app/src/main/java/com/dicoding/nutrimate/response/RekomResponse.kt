package com.dicoding.nutrimate.response

import com.google.gson.annotations.SerializedName

data class RekomResponse(

	@field:SerializedName("data")
	val data: DataRekom? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataRekom(

	@field:SerializedName("menggemukkan")
	val menggemukkan: Any? = null,

	@field:SerializedName("mengidealkan")
	val mengidealkan: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("program")
	val program: String? = null,

	@field:SerializedName("menguruskan")
	val menguruskan: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)
