package com.qello.data.remote.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenHolder @Inject constructor() {
    @Volatile
    private var accessToken: String? = null

    fun get(): String? = accessToken

    fun update(token: String) {
        accessToken = token
    }

    fun clear() {
        accessToken = null
    }
}
