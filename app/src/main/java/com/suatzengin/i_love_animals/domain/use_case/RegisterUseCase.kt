package com.suatzengin.i_love_animals.domain.use_case

import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: FirebaseAuthRepository
) {
    operator fun invoke(email: String?, password: String?) = flow {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            emit(Resource.Error(message = "Email or password must not be empty!"))
            return@flow
        }
        emit(repository.register(email = email, password = password))
    }
}