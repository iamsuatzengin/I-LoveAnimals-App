package com.suatzengin.i_love_animals.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.ktx.toObject
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.model.Filter
import com.suatzengin.i_love_animals.domain.model.User
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.util.Constants.ADVERTISEMENT
import com.suatzengin.i_love_animals.util.Constants.USER
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDbRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FirebaseDbRepository {

    override suspend fun postNewAd(advertisement: Advertisement) {
        val document = firestore.collection(ADVERTISEMENT).document()
        advertisement.id = document.id
        document.set(advertisement).await()
    }

    override fun getAllAd(
        direction: Direction,
        status: Boolean,
        filterByUser: Filter?
    ): Flow<Resource<List<Advertisement>>> = callbackFlow {
        val collection = firestore.collection(ADVERTISEMENT)
            .orderBy("date", direction)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    trySend(
                        Resource.Error(
                            message = error.localizedMessage ?: "Something went wrong!"
                        )
                    )
                    return@addSnapshotListener
                }
                var adList = ArrayList<Advertisement>()
                for (document in snapshots!!) {
                    val myAdvertisement = document.toObject<Advertisement>()
                    adList.add(myAdvertisement)
                }
                adList = adList.filter {
                    it.status == status
                } as ArrayList<Advertisement>
                when (filterByUser) {
                    Filter.ALL -> {}
                    Filter.MINE -> {
                        adList = adList
                            .filter {
                                it.authorEmail == auth.currentUser?.email
                            } as ArrayList<Advertisement>
                    }
                }
                trySend(
                    Resource.Success(
                        data = adList
                    )
                )
            }
        awaitClose { collection.remove() }
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

    override suspend fun getUser(email: String): User? {
        val docRef = firestore.collection(USER).document(email)
        val getDoc = docRef.get().await()
        println("user from repo : ${getDoc.toObject<User>()}")
        return getDoc.toObject<User>()
    }

    override suspend fun updateUserAdCount(email: String) {
        firestore.collection(USER).document(email).update(
            "adCount", FieldValue.increment(1)
        ).await()
    }

    override suspend fun updateUserAdCompletedCount(email: String) {
        firestore.collection(USER).document(email).update(
            "completedAdCount", FieldValue.increment(1)
        )
    }
}