package com.suatzengin.i_love_animals.domain.use_case.ad

import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.util.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostAdUseCase @Inject constructor(
    private val repository: FirebaseDbRepository
) {
    operator fun invoke(advertisement: Advertisement) = flow {
        emit(Resource.Loading())
        try {
            if (
                advertisement.title.isNullOrEmpty() ||
                advertisement.description.isNullOrEmpty() ||
                advertisement.location == null
            ) {
                emit(Resource.Error(message = "Gerekli alanlar boş kalmamalı!"))
                return@flow
            }
            repository.postNewAd(advertisement)
            emit(Resource.Success(data = "Başarılı"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Error"))
        }
    }
}