package com.dicoding.nutrimate.response

import com.google.gson.annotations.SerializedName

data class ProgressResponse(

	@field:SerializedName("progress")
	val progress: Progress? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Progress(

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("height")
	val height: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
