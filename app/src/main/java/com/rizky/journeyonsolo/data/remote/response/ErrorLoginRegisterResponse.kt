package com.rizky.journeyonsolo.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorLoginRegisterResponse(

	@field:SerializedName("detail")
	val detail: Detail
)

data class Detail(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: String
)