package com.suatzengin.i_love_animals.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.domain.use_case.ad.ChangeAdStatus
import com.suatzengin.i_love_animals.domain.use_case.ad.GetAllAdUseCase
import com.suatzengin.i_love_animals.domain.use_case.ad.PostAdUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.GetUserUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.LoginUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.RegisterUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.SignOutUseCase
import com.suatzengin.i_love_animals.domain.use_case.user.ProfileUseCase
import com.suatzengin.i_love_animals.domain.use_case.user.UpdateUserAdCompletedCount
import com.suatzengin.i_love_animals.domain.use_case.user.UpdateUserAdCount
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideUseCases(
        authRepository: FirebaseAuthRepository,
        dbRepository: FirebaseDbRepository
    ) = UseCases(
        loginUseCase = LoginUseCase(repository = authRepository),
        registerUseCase = RegisterUseCase(repository = authRepository),
        getUserUseCase = GetUserUseCase(repository = authRepository),
        signOutUseCase = SignOutUseCase(repository = authRepository),
        postAdUseCase = PostAdUseCase(repository = dbRepository),
        getAllAdUseCase = GetAllAdUseCase(repository = dbRepository),
        changeAdStatus = ChangeAdStatus(repository = dbRepository),
        profileUseCase = ProfileUseCase(repository = dbRepository),
        updateUserAdCompletedCount = UpdateUserAdCompletedCount(repository = dbRepository),
        updateUserAdCount = UpdateUserAdCount(repository = dbRepository)
    )
}