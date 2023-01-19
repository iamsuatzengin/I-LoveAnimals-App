package com.suatzengin.i_love_animals.domain.repository


import com.google.firebase.firestore.Query.Direction
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseDbRepository {

    suspend fun postNewAd(advertisement: Advertisement)
    fun getAllAd(direction: Direction, status: Boolean): Flow<Resource<List<Advertisement>>>
    suspend fun changeStatus(id: String, status: Boolean): Resource<String>
}