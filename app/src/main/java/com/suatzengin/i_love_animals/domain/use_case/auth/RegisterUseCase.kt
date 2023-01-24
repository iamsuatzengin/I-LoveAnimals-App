package com.suatzengin.i_love_animals.domain.use_case.auth

import com.suatzengin.i_love_animals.domain.model.User
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: FirebaseAuthRepository
) {
    operator fun invoke(user: User) = flow {
        if (user.email.isEmpty() || user.password.isEmpty() || user.fullName.isEmpty()) {
            emit(Resource.Error(message = "Email or password must not be empty!"))
            return@flow
        }
        emit(
            repository.register(user = user)
        )
    }
}