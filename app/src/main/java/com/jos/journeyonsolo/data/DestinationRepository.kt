package com.jos.journeyonsolo.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.jos.journeyonsolo.R
import com.jos.journeyonsolo.data.local.entity.FavoriteDestination
import com.jos.journeyonsolo.data.local.room.DestinationDao
import com.jos.journeyonsolo.data.pref.Session
import com.jos.journeyonsolo.data.remote.response.DestinationDetailResponse
import com.jos.journeyonsolo.data.remote.response.ListDestinationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException

class DestinationRepository(
    private val session: Session,
    private val destinationDao: DestinationDao
) {
    private val db = Firebase.firestore

    suspend fun logout() = session.logout()

    fun getAllDestination(context: Context) = liveData {
        emit(Result.Loading)
        try {
            val result = db.collection("data").get().await()
//            Log.d(TAG, "DestinationRepository: $result") // Menggunakan await
            val response = result.documents.mapNotNull { document ->
                val data = document.data
                if (data != null) {
                    try {
                        val placeId = data["place_id"] as? String ?: ""
                        val imageResId = getImageResourceId(context,placeId)

                        ListDestinationItem(
                            placeId = placeId,
                            name = data["name"] as? String ?: "",
                            address = data["address"] as? String ?: "",
                            imageUrl = imageResId.toString(), // Simpan ID sebagai string
                            rating = (data["rating"] as? Number)?.toString() ?: "",
                            category = data["category"] as? String ?: "",
                            reviewsCount = (data["reviews_count"] as? Number)?.toString() ?: "",
                            lat = (data["lat"] as? Number)?.toFloat() ?: 0f,
                            lon = (data["long"] as? Number)?.toFloat() ?: 0f,
                            captionIdn = data["caption_idn"] as? String ?: "",
                            captionEng = data["caption_eng"] as? String ?: ""
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error mapping document ${document.id}: ${e.message}")
                        null // Pastikan return null jika terjadi error
                    }
                } else {
                    null // Kembalikan null jika data tidak ada
                }
            }
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error("Response error"))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }

    /**
     * Fungsi untuk mendapatkan resource ID gambar dari drawable berdasarkan placeId.
     */
    private fun getImageResourceId(context: Context, placeId: String): Int {
        val resourceName = "a$placeId"
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.logojos
    }


    fun getDestinationId(id: String, context: Context) = liveData {
        emit(Result.Loading)
        try {
            val result = db.collection("data").whereEqualTo("place_id", id).get().await()

            // Ambil dokumen pertama (atau null jika tidak ada dokumen)
            val document = result.documents.firstOrNull()
            if (document != null) {
                val data = document.data

                // Mapping dokumen ke DestinationDetailResponse
                if (data != null) {
                    try {
                        val placeId = data["place_id"] as? String ?: ""
                        val imageResId = getImageResourceId(context,placeId)

                        val response = DestinationDetailResponse(
                            placeId = placeId,
                            name = data["name"] as? String ?: "",
                            address = data["address"] as? String ?: "",
                            imageUrl = imageResId.toString(),
                            rating = (data["rating"] as? Number)?.toString() ?: "",
                            category = data["category"] as? String ?: "",
                            reviewsCount = (data["reviews_count"] as? Number)?.toString() ?: "",
                            lat = (data["lat"] as? Number)?.toString() ?: "",
                            lon = (data["long"] as? Number)?.toString() ?: "",
                            captionIdn = data["caption_idn"] as? String ?: "",
                            captionEng = data["caption_eng"] as? String ?: ""
                        )
                        emit(Result.Success(response))
                    } catch (e: Exception) {
                        Log.e(TAG, "Error mapping document ${document.id}: ${e.message}")
                        emit(Result.Error("Mapping error"))
                    }
                } else {
                    Log.w(TAG, "Document data is null for ID: ${document.id}")
                    emit(Result.Error("No data found"))
                }
            } else {
                Log.w(TAG, "No document found for ID: $id")
                emit(Result.Error("Document not found"))
            }
        } catch (e: HttpException) {
            emit(Result.Error("Response error"))
        } catch (e: Exception) {
            emit(Result.Error("Lost Connection"))
        }
    }


    fun searchDestination(keyword: String, context: Context): LiveData<Result<List<ListDestinationItem>>> {
        return liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val result = db.collection("data").get().await()
                val response = result.documents.mapNotNull { document ->
                    val data = document.data
                    if (data != null) {
                        try {
                            val placeId = data["place_id"] as? String ?: ""
                            val imageResId = getImageResourceId(context, placeId)

                            ListDestinationItem(
                                placeId = placeId,
                                name = data["name"] as? String ?: "",
                                address = data["address"] as? String ?: "",
                                imageUrl = imageResId.toString(),
                                rating = (data["rating"] as? Number)?.toString() ?: "",
                                category = data["category"] as? String ?: "",
                                reviewsCount = (data["reviews_count"] as? Number)?.toString() ?: "",
                                lat = (data["lat"] as? Number)?.toFloat() ?: 0f,
                                lon = (data["long"] as? Number)?.toFloat() ?: 0f,
                                captionIdn = data["caption_idn"] as? String ?: "",
                                captionEng = data["caption_eng"] as? String ?: ""
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Error mapping document ${document.id}: ${e.message}")
                            null
                        }
                    } else {
                        null
                    }
                }.filter { it.name.contains(keyword, ignoreCase = true) }

                emit(Result.Success(response))
            } catch (e: HttpException) {
                emit(Result.Error("Response error"))
            } catch (e: Exception) {
                emit(Result.Error("Lost Connection"))
            }
        }
    }


    fun getFavoriteDestination(): LiveData<List<FavoriteDestination>> {
        return destinationDao.getFavoriteDestination()
    }

    suspend fun setInsertFavoriteDestination(destination: FavoriteDestination) {
        coroutineScope {
            launch(Dispatchers.IO) {
                destinationDao.insert(destination)
            }
        }
    }

    suspend fun delete(destination: FavoriteDestination) {
        coroutineScope {
            launch(Dispatchers.IO) {
                destinationDao.delete(destination)
            }
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return session.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        session.saveThemeSetting(isDarkModeActive)

    fun getIsFavorite(id: String): LiveData<Boolean> = destinationDao.isFavoriteDestination(id)

    companion object {

        private const val TAG = "DestinationRepository"

        @SuppressLint("StaticFieldLeak")
        private var instance: DestinationRepository? = null
        fun getInstance(
            session: Session,
            destinationDao: DestinationDao
        ): DestinationRepository =
            instance ?: synchronized(this) {
                instance ?: DestinationRepository(session, destinationDao)
            }.also { instance = it }
    }

}