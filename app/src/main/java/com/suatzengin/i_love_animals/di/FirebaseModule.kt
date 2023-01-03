package com.suatzengin.i_love_animals.di

import com.google.firebase.auth.FirebaseAuth
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.domain.use_case.auth.GetUserUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.LoginUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.RegisterUseCase
import com.suatzengin.i_love_animals.domain.use_case.auth.SignOutUseCase
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
    fun provideUseCases(
        repository: FirebaseAuthRepository
    ) = UseCases(
        loginUseCase = LoginUseCase(repository = repository),
        registerUseCase = RegisterUseCase(repository = repository),
        getUserUseCase = GetUserUseCase(repository = repository),
        signOutUseCase = SignOutUseCase(repository = repository)
    )
}