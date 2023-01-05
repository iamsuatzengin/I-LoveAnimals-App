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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDbRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseDbRepository {


    override fun postNewAd(advertisement: Advertisement) = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(ADVERTISEMENT)
                .document()
                .set(advertisement)
                .await()
            emit(Resource.Success(data = "Successfully added!"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Something went wrong!"))
        }
    }

    override fun getAllAd(
        direction: Direction,
        status: Boolean
    ): Flow<Resource<List<Advertisement>>> =
        callbackFlow {
            try {
                firestore.collection(ADVERTISEMENT)
                    .orderBy("date", direction)
                    .get()
                    .addOnSuccessListener { result ->
                        val adList = ArrayList<Advertisement>()
                        for (document in result) {
                            println("document: ${document.id} => ${document.data}")
                            val ad = document.toObject<Advertisement>()
                            adList.add(ad)
                        }
                        trySend(Resource.Success(data = adList))
                    }
                    .addOnFailureListener {
                        trySend(
                            Resource.Error(
                                message = it.localizedMessage ?: "Failure Listener"
                            )
                        )
                    }
            } catch (e: Exception) {
                trySend(Resource.Error(message = e.localizedMessage ?: "Something went wrong!"))
            }
            awaitClose { close() }
        }

}