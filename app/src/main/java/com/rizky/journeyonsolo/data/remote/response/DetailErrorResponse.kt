package com.rizky.journeyonsolo.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailErrorResponse(

	@field:SerializedName("detail")
	val detail: String
)