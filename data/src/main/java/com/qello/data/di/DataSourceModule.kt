package com.qello.data.di

import com.qello.data.remote.datasource.AuthDataSource
import com.qello.data.remote.datasource.QuestionDataSource
import com.qello.data.remote.datasource.impl.AuthDataSourceImpl
import com.qello.data.remote.datasource.impl.QuestionDataSourceImpl
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
}
