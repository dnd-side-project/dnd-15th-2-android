package com.qello.data.remote.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateNicknameRequest(
    val nickname: String,
)
