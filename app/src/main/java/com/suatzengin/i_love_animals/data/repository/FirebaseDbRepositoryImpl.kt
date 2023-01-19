package com.suatzengin.i_love_animals.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.ktx.toObject
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.util.Constants.ADVERTISEMENT
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDbRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseDbRepository {


    override suspend fun postNewAd(advertisement: Advertisement) {
        val document = firestore.collection(ADVERTISEMENT).document()
        advertisement.id = document.id
        document.set(advertisement).await()
    }

    override fun getAllAd(
        direction: Direction,
        status: Boolean
    ): Flow<Resource<List<Advertisement>>> = callbackFlow {
        firestore.collection(ADVERTISEMENT)
            .orderBy("date", direction)
            .get()
            .addOnSuccessListener { result ->
                val adList = ArrayList<Advertisement>()
                for (document in result) {
                    val myAdvertisement = document.toObject<Advertisement>()
                    adList.add(myAdvertisement)
                }
                trySend(Resource.Success(data = adList.filter { it.status == status }))
            }.addOnFailureListener { e ->
                trySend(
                    Resource.Error(
                        message = e.localizedMessage ?: "Something went wrong!"
                    )
                )
            }
        awaitClose { close() }
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