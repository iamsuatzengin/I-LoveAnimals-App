package com.suatzengin.i_love_animals.domain.use_case.ad

import com.google.firebase.firestore.Query.Direction
import com.suatzengin.i_love_animals.domain.model.Filter
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import javax.inject.Inject

class GetAllAdUseCase @Inject constructor(
    private val repository: FirebaseDbRepository
) {
    operator fun invoke(direction: Direction, status: Boolean = false, filterByUser: Filter?) =
        repository.getAllAd(direction = direction, status = status, filterByUser = filterByUser)
}