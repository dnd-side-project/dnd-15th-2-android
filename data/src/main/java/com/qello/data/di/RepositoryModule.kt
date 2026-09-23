package com.qello.data.di

import com.qello.data.repository.UserAccountRepositoryImpl
import com.qello.domain.repository.UserAccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsUserAccountRepository(
        impl: UserAccountRepositoryImpl,
    ): UserAccountRepository
}
