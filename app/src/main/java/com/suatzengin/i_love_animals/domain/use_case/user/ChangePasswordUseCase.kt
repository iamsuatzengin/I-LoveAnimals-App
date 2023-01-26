package com.suatzengin.i_love_animals.domain.use_case.user

import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: FirebaseAuthRepository
) {
    suspend operator fun invoke(newPassword: String?): String{
        if(newPassword.isNullOrEmpty()) return "Şifre boş olamaz!"
        return repository.changePassword(newPassword = newPassword)
    }
}