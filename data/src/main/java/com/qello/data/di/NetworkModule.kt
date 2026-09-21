package com.qello.data.di

import com.qello.data.BuildConfig
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
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.IOException
import javax.inject.Singleton

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
    internal fun providesHttpClient(json: Json): HttpClient = HttpClient(OkHttp) {
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

        // TODO(다음 단계): install(Auth) { bearer { ... } } — 토큰 헤더 부착과 401 재발급

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
}
