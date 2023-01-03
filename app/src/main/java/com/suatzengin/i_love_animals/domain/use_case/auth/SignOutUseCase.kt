package com.suatzengin.i_love_animals.domain.use_case.auth

import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: FirebaseAuthRepository
) {
    operator fun invoke() { repository.signOut() }
}