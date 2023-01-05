package com.suatzengin.i_love_animals.di

import com.suatzengin.i_love_animals.data.repository.FirebaseAuthRepositoryImpl
import com.suatzengin.i_love_animals.data.repository.FirebaseDbRepositoryImpl
import com.suatzengin.i_love_animals.domain.repository.FirebaseAuthRepository
import com.suatzengin.i_love_animals.domain.repository.FirebaseDbRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFirebaseAuthRepository(firebaseAuthRepositoryImpl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseDbRepository(firebaseDbRepositoryImpl: FirebaseDbRepositoryImpl): FirebaseDbRepository
}