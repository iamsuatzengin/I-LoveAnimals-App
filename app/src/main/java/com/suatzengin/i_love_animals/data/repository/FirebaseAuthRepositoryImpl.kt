package com.suatzengin.i_love_animals.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseAuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun register(
        email: String, password: String, fullName: String
    ): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email,password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = fullName
            }
            result.user?.updateProfile(profileUpdates)

            Resource.Success(result.user ?: throw FirebaseAuthException("","Register failed!"))
        }catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Register failed!")
        }
    }

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user ?: throw FirebaseAuthException("","Login failed!"))
        }catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Login failed!")
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}