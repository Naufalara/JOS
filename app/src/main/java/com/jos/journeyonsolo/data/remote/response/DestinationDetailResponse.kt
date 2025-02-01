package com.jos.journeyonsolo.data.remote.response

import com.google.gson.annotations.SerializedName

data class DestinationDetailResponse(

	@field:SerializedName("place_id")
	val placeId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("reviews_count")
	val reviewsCount: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("long")
	val lon: String,

	@field:SerializedName("caption_idn")
	val captionIdn: String,

	@field:SerializedName("caption_eng")
	val captionEng: String,
)