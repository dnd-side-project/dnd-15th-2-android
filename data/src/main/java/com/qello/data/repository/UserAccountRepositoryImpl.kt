package com.qello.data.repository

import com.qello.data.local.datastore.UserPreferencesDataSource
import com.qello.domain.model.UserAccount
import com.qello.domain.repository.UserAccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserAccountRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserAccountRepository {

    override val userAccount: Flow<UserAccount> = userPreferencesDataSource.userAccount

    override suspend fun createAccount(nickname: String) =
        userPreferencesDataSource.saveAccount(nickname)
}
