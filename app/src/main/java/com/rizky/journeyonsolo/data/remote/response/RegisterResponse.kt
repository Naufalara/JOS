package com.rizky.journeyonsolo.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("token")
	val token: String
)