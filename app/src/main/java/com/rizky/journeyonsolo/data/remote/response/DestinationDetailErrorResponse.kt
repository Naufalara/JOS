package com.rizky.journeyonsolo.data.remote.response

import com.google.gson.annotations.SerializedName

data class DestinationDetailErrorResponse(

	@field:SerializedName("detail")
	val detail: String
)