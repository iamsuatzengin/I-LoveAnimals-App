package com.suatzengin.i_love_animals.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.model.User
import com.suatzengin.i_love_animals.util.Resource


interface FirebaseAuthRepository {
    val currentUser: FirebaseUser?
    suspend fun register(user: User): Resource<FirebaseUser>
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    fun signOut()
    suspend fun sendPasswordResetEmail(email: String): String
}