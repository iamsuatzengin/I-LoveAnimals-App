package com.suatzengin.i_love_animals.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDbRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseDbRepository {


    override fun postNewAd(advertisement: Advertisement) = flow {
        emit(Resource.Loading())
        try {
            firestore.collection("advertisement")
                .document()
                .set(advertisement)
                .await()
            emit(Resource.Success(data = "Successfully added!"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Something went wrong!"))
        }
    }
}