package com.suatzengin.i_love_animals.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.suatzengin.i_love_animals.domain.model.User
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.util.Constants.USER
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirebaseAuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun register(user: User): Resource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = user.fullName
            }
            result.user?.updateProfile(profileUpdates)
            firestore.collection(USER).document(user.email).set(user)
            Resource.Success(result.user ?: throw FirebaseAuthException("", "Register failed!"))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Register failed!")
        }
    }

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user ?: throw FirebaseAuthException("", "Login failed!"))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Login failed!")
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String): String {
        return try {
            auth.sendPasswordResetEmail(email).await()
            "Mail G??nderildi"
        } catch (e: Exception) {
            "E-mail yanl???? olabilir!"
        }
    }

    override suspend fun changePassword(newPassword: String): String {
        return try {
            val user = auth.currentUser
            user!!.updatePassword(newPassword).await()
            val credential = EmailAuthProvider.getCredential(user.email!!,newPassword)
            user.reauthenticate(credential).await()
            "Ba??ar??l?? ??ekilde de??i??tirildi!"
        }catch (e: Exception){
            "Bir hata olu??tu"
        }
    }
}