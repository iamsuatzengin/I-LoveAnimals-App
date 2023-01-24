package com.suatzengin.i_love_animals.domain.use_case.user

import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import javax.inject.Inject

class UpdateUserAdCount @Inject constructor(
    private val repository: FirebaseDbRepository
) {
    suspend operator fun invoke(email: String) =
        repository.updateUserAdCount(email = email)
}