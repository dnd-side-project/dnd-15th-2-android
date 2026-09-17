package com.qello.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * DataStore<Preferences>는 어떤 스토어든 타입이 같아서, 두 번째 스토어가 생기면
 * Hilt 그래프에서 구분이 불가능해진다.
 * 따라서 구분용 어노테이션 생성
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserPreferences

private const val USER_PREFERENCES_NAME = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    /**
     * NIA DataStoreModule과 동일한 구조: @Dispatcher(IO) + @ApplicationScope를 주입해 팩토리로 직접 생성한다.
     * corruptionHandler는 공식 DataStore 가이드의 손상 처리 권고에 따른 것으로,
     * 파일이 깨졌을 때 크래시 대신 빈 값으로 복구한다.
     */
    @Provides
    @Singleton
    @UserPreferences
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(QelloDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
    ) {
        context.preferencesDataStoreFile(USER_PREFERENCES_NAME)
    }
}
