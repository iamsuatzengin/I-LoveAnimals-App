package com.suatzengin.i_love_animals.domain.use_case.ad

import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class PasswordResetUseCase @Inject constructor(
    private val repository: FirebaseAuthRepository
) {
    suspend operator fun invoke(email: String?): String {
        if(email.isNullOrEmpty()) return "Boş olamaz!"
        return repository.sendPasswordResetEmail(email = email)
    }
}