package com.suatzengin.i_love_animals.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: FirebaseAuthRepository
) {
    fun getCurrentUser(): FirebaseUser? = repository.currentUser

    operator fun invoke(email: String?, password: String?) = flow {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            emit(Resource.Error(message = "Email or password must not be empty!"))
            return@flow
        }
        emit(repository.login(email = email, password = password))
    }
}