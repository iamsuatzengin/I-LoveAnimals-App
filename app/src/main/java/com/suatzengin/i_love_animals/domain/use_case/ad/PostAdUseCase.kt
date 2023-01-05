package com.suatzengin.i_love_animals.domain.use_case.ad

import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import javax.inject.Inject

class PostAdUseCase @Inject constructor(
    private val repository: FirebaseDbRepository
) {
    operator fun invoke(advertisement: Advertisement) = repository.postNewAd(advertisement)
}