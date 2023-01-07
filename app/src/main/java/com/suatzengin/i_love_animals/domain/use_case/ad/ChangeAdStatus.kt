package com.suatzengin.i_love_animals.domain.use_case.ad

import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.util.Resource
import javax.inject.Inject

class ChangeAdStatus @Inject constructor(
    private val repository: FirebaseDbRepository
) {
    suspend operator fun invoke(id: String, status: Boolean): Resource<String> =
        repository.changeStatus(id = id, status = status)
}