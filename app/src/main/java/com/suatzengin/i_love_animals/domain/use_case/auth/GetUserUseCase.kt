package com.suatzengin.i_love_animals.domain.use_case.auth

import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: FirebaseAuthRepository
){
    operator fun invoke(): FirebaseUser? = repository.currentUser
}