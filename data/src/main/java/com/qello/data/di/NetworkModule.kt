package com.qello.data.di

import com.qello.data.BuildConfig
import com.qello.data.local.datastore.UserPreferencesDataSource
import com.qello.data.remote.auth.TokenRefresher
import com.qello.data.remote.response.ErrorResponse
import com.qello.domain.result.AppError
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.IOException
import javax.inject.Qualifier
import javax.inject.Singleton

/** 우리 API 서버가 아니라, 발급받은 업로드 주소(presigned URL) 등 외부 주소로 직접 요청할 때 쓰는 클라이언트 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RawHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    internal fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    internal fun providesHttpClient(
        json: Json,
        userPreferencesDataSource: UserPreferencesDataSource,
        tokenRefresher: TokenRefresher,
    ): HttpClient = HttpClient(OkHttp) {
        expectSuccess = false

        defaultRequest {
            url(BuildConfig.BASE_URL)
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 10_000L
            requestTimeoutMillis = 15_000L
            socketTimeoutMillis = 15_000L
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag("Ktor").d(message)
                }
            }
            level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }

        install(Auth) {
            bearer {
                cacheTokens = false

                nonCancellableRefresh = true

                loadTokens {
                    userPreferencesDataSource.getAccessToken()?.let { token ->
                        BearerTokens(accessToken = token, refreshToken = null)
                    }
                }

                refreshTokens {
                    tokenRefresher.refresh(this)
                }

                sendWithoutRequest { request ->
                    !request.url.encodedPath.contains("/auth/")
                }
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.isSuccess()) return@validateResponse

                // 게이트웨이 HTML 등 JSON이 아닌 에러 바디는 필드를 비운 채 status만 전달한다
                val body = try {
                    response.body<ErrorResponse>()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    null
                }
                throw AppError.Server(
                    status = response.status.value,
                    code = body?.errorDetail?.code,
                    field = body?.errorDetail?.field,
                    reason = body?.errorDetail?.reason,
                    message = body?.message,
                )
            }

            handleResponseExceptionWithRequest { cause, _ ->
                if (cause is IOException) throw AppError.Network(cause)
            }
        }
    }

    @Provides
    @Singleton
    @RawHttpClient
    internal fun providesRawHttpClient(): HttpClient = HttpClient(OkHttp) {
        expectSuccess = false

        install(HttpTimeout) {
            connectTimeoutMillis = 10_000L
            requestTimeoutMillis = 30_000L
            socketTimeoutMillis = 30_000L
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag("Ktor-Raw").d(message)
                }
            }
            level = if (BuildConfig.DEBUG) LogLevel.HEADERS else LogLevel.NONE
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw AppError.Server(
                        status = response.status.value,
                        code = null,
                        field = null,
                        reason = null,
                        message = null,
                    )
                }
            }

            handleResponseExceptionWithRequest { cause, _ ->
                if (cause is IOException) throw AppError.Network(cause)
            }
        }
    }
}
