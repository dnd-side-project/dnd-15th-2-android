package com.qello.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.qello.data.di.UserPreferences
import com.qello.domain.model.DeviceCredential
import com.qello.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    @UserPreferences private val dataStore: DataStore<Preferences>,
) {
    val userAccount: Flow<UserAccount> = dataStore.data.map { preferences ->
        UserAccount(
            installationId = preferences[Keys.INSTALLATION_ID],
            userId = preferences[Keys.USER_ID],
            nickname = preferences[Keys.NICKNAME],
        )
    }

    suspend fun getOrCreateInstallationId(): String {
        val preferences = dataStore.edit { preferences ->
            if (preferences[Keys.INSTALLATION_ID].isNullOrBlank()) {
                preferences[Keys.INSTALLATION_ID] = createInstallationId()
            }
        }
        return checkNotNull(preferences[Keys.INSTALLATION_ID])
    }

    suspend fun saveAccount(
        nickname: String,
        userId: String,
        deviceSecret: String,
    ) {
        dataStore.edit { preferences ->
            preferences[Keys.USER_ID] = userId
            preferences[Keys.NICKNAME] = nickname
            preferences[Keys.DEVICE_SECRET] = deviceSecret
        }
    }

    suspend fun getDeviceCredential(): DeviceCredential? {
        val preferences = dataStore.data.first()
        val installationId = preferences[Keys.INSTALLATION_ID] ?: return null
        val deviceSecret = preferences[Keys.DEVICE_SECRET] ?: return null
        return DeviceCredential(installationId, deviceSecret)
    }

    suspend fun getAccessToken(): String? = dataStore.data.first()[Keys.ACCESS_TOKEN]

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences -> preferences[Keys.ACCESS_TOKEN] = accessToken }
    }

    suspend fun clearAccount() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.INSTALLATION_ID)
            preferences.remove(Keys.USER_ID)
            preferences.remove(Keys.NICKNAME)
            preferences.remove(Keys.DEVICE_SECRET)
            preferences.remove(Keys.ACCESS_TOKEN)
        }
    }

    private fun createInstallationId(): String =
        UUID.randomUUID().toString().replace("-", "")

    private object Keys {
        val INSTALLATION_ID = stringPreferencesKey("installationId")
        val USER_ID = stringPreferencesKey("userId")
        val NICKNAME = stringPreferencesKey("nickname")
        val DEVICE_SECRET = stringPreferencesKey("deviceSecret")
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
    }
}
