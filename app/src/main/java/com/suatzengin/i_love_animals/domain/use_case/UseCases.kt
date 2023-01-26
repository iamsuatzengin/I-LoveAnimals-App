package com.suatzengin.i_love_animals.domain.use_case

import com.suatzengin.i_love_animals.domain.use_case.ad.ChangeAdStatus
import com.suatzengin.i_love_animals.domain.use_case.ad.GetAllAdUseCase
import com.suatzengin.i_love_animals.domain.use_case.ad.PasswordResetUseCase
import com.suatzengin.i_love_animals.domain.use_case.ad.PostAdUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.GetUserUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.LoginUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.RegisterUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.SignOutUseCase
import com.suatzengin.i_love_animals.domain.use_case.user.ProfileUseCase
import com.suatzengin.i_love_animals.domain.use_case.user.UpdateUserAdCompletedCount
import com.suatzengin.i_love_animals.domain.use_case.user.UpdateUserAdCount
import javax.inject.Inject

data class UseCases @Inject constructor(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val getUserUseCase: GetUserUseCase,
    val signOutUseCase: SignOutUseCase,
    val postAdUseCase: PostAdUseCase,
    val getAllAdUseCase: GetAllAdUseCase,
    val changeAdStatus: ChangeAdStatus,
    val profileUseCase: ProfileUseCase,
    val updateUserAdCompletedCount: UpdateUserAdCompletedCount,
    val updateUserAdCount: UpdateUserAdCount,
    val passwordResetUseCase: PasswordResetUseCase
)
