package com.qello.data.di

import com.qello.data.repository.DirectionRepositoryImpl
import com.qello.data.repository.InboxRepositoryImpl
import com.qello.data.repository.MediaRepositoryImpl
import com.qello.data.repository.QuestionRepositoryImpl
import com.qello.data.repository.SentPostRepositoryImpl
import com.qello.data.repository.UserAccountRepositoryImpl
import com.qello.domain.repository.DirectionRepository
import com.qello.domain.repository.InboxRepository
import com.qello.domain.repository.MediaRepository
import com.qello.domain.repository.QuestionRepository
import com.qello.domain.repository.SentPostRepository
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

    @Binds
    @Singleton
    fun bindsQuestionRepository(
        impl: QuestionRepositoryImpl,
    ): QuestionRepository

    @Binds
    @Singleton
    fun bindsMediaRepository(
        impl: MediaRepositoryImpl,
    ): MediaRepository

    @Binds
    @Singleton
    fun bindsDirectionRepository(
        impl: DirectionRepositoryImpl,
    ): DirectionRepository

    @Binds
    @Singleton
    fun bindsInboxRepository(
        impl: InboxRepositoryImpl,
    ): InboxRepository

    @Binds
    @Singleton
    fun bindsSentPostRepository(
        impl: SentPostRepositoryImpl,
    ): SentPostRepository
}
