package com.qello.data.remote.auth

import com.qello.data.local.datastore.UserPreferencesDataSource
import com.qello.data.remote.request.RefreshTokenRequest
import com.qello.data.remote.response.ApiResponse
import com.qello.data.remote.response.RefreshTokenResponse
import com.qello.domain.result.AppError
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class TokenRefresher @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) {

    suspend fun refresh(params: RefreshTokensParams): BearerTokens? {
        val credential = userPreferencesDataSource.getDeviceCredential() ?: return null

        val token = try {
            params.client.post("auth/token") {
                with(params) { markAsRefreshTokenRequest() }
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(credential.installationId, credential.deviceSecret))
            }.body<ApiResponse<RefreshTokenResponse>>().data
        } catch (e: AppError.Server) {
            if (e.status == 401) {
                userPreferencesDataSource.clearAccount()
                return null
            }
            throw e
        }

        userPreferencesDataSource.saveAccessToken(token.accessToken)
        return BearerTokens(accessToken = token.accessToken, refreshToken = null)
    }
}
