package com.qello.domain.repository

import com.qello.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow

interface UserAccountRepository {
    val userAccount: Flow<UserAccount>

    suspend fun createAccount(nickname: String)
}
