package com.qello.data.di

import com.qello.data.permission.AndroidPermissionChecker
import com.qello.domain.permission.PermissionChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PermissionModule {

    @Binds
    @Singleton
    fun bindsPermissionChecker(
        impl: AndroidPermissionChecker,
    ): PermissionChecker
}
