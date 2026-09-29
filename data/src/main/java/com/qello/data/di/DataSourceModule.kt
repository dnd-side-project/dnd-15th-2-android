package com.qello.data.di

import com.qello.data.remote.datasource.AuthDataSource
import com.qello.data.remote.datasource.DirectionDataSource
import com.qello.data.remote.datasource.InboxDataSource
import com.qello.data.remote.datasource.MediaDataSource
import com.qello.data.remote.datasource.QuestionDataSource
import com.qello.data.remote.datasource.SentPostDataSource
import com.qello.data.remote.datasource.impl.AuthDataSourceImpl
import com.qello.data.remote.datasource.impl.DirectionDataSourceImpl
import com.qello.data.remote.datasource.impl.InboxDataSourceImpl
import com.qello.data.remote.datasource.impl.MediaDataSourceImpl
import com.qello.data.remote.datasource.impl.QuestionDataSourceImpl
import com.qello.data.remote.datasource.impl.SentPostDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsAuthDataSource(
        impl: AuthDataSourceImpl,
    ): AuthDataSource

    @Binds
    fun bindsQuestionDataSource(
        impl: QuestionDataSourceImpl,
    ): QuestionDataSource

    @Binds
    fun bindsMediaDataSource(
        impl: MediaDataSourceImpl,
    ): MediaDataSource

    @Binds
    fun bindsDirectionDataSource(
        impl: DirectionDataSourceImpl,
    ): DirectionDataSource

    @Binds
    fun bindsInboxDataSource(
        impl: InboxDataSourceImpl,
    ): InboxDataSource

    @Binds
    fun bindsSentPostDataSource(
        impl: SentPostDataSourceImpl,
    ): SentPostDataSource
}
