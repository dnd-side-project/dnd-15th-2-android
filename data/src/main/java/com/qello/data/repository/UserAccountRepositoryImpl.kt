package com.qello.data.repository

import com.qello.data.local.datastore.UserPreferencesDataSource
import com.qello.data.remote.datasource.AuthDataSource
import com.qello.data.remote.model.auth.RegisterDeviceRequest
import com.qello.domain.model.UserAccount
import com.qello.domain.repository.UserAccountRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserAccountRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val authDataSource: AuthDataSource,
) : UserAccountRepository {

    override val userAccount: Flow<UserAccount> = userPreferencesDataSource.userAccount

    override suspend fun createAccount(nickname: String) {
        val installationId = userPreferencesDataSource.getOrCreateInstallationId()
        val response = authDataSource.registerDevice(
            RegisterDeviceRequest(
                installationId = installationId,
                nickname = nickname,
                platform = "ANDROID",
                timezone = "Asia/Seoul",
                countryCode = "KR",
                coarseRegionCode = "KR-11",
                locale = "ko_KR",
            ),
        )

        withContext(NonCancellable) {
            userPreferencesDataSource.saveAccount(
                userId = response.userId.toString(),
                nickname = nickname,
                deviceSecret = response.deviceSecret,
            )
        }
    }

    override suspend fun isRegistered(): Boolean =
        userPreferencesDataSource.getDeviceCredential() != null

    override suspend fun clearAccount() {
        userPreferencesDataSource.clearAccount()
    }
}
