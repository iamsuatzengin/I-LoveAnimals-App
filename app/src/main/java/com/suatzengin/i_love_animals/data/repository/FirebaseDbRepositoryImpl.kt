package com.suatzengin.i_love_animals.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.ktx.toObject
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.util.Constants.ADVERTISEMENT
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDbRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseDbRepository {


    override fun postNewAd(advertisement: Advertisement) = flow {
        emit(Resource.Loading())
        try {

            val document = firestore.collection(ADVERTISEMENT).document()
            advertisement.id = document.id
            document.set(advertisement).await()
            emit(Resource.Success(data = "Successfully added!"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Something went wrong!"))
        }
    }

    override fun getAllAd(
        direction: Direction,
        status: Boolean
    ): Flow<Resource<List<Advertisement>>> =
        flow {
            try {
                val result = firestore.collection(ADVERTISEMENT)
                    .orderBy("date", direction)
                    .get().await()
                val adList = ArrayList<Advertisement>()
                for (document in result) {
                    val myAdvertisement = document.toObject<Advertisement>()
                    adList.add(myAdvertisement)
                }
                emit(
                    Resource.Success(
                        data = adList.filter {
                            it.status == status
                        })
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: "Something went wrong!"))
            }
        }

    override suspend fun changeStatus(id: String, status: Boolean): Resource<String> {
        return try {
            firestore.collection(ADVERTISEMENT)
                .document(id)
                .update("status", status)
                .await()
            Resource.Success(data = "Beslendi")
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong!")
        }
    }
}