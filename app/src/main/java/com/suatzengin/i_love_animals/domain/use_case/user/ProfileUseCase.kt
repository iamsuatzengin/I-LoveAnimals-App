package com.suatzengin.i_love_animals.domain.use_case.user

import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val repository: FirebaseDbRepository
) {
    operator fun invoke(email: String) = flow {
        try {
            val user = repository.getUser(email = email)
            emit(Resource.Success(data = user))
        }catch (e: Exception){
            emit(Resource.Error(message = e.localizedMessage ?: "Error" ))
        }
    }
}