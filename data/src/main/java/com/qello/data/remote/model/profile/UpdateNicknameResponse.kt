package com.qello.data.remote.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateNicknameResponse(
    val nickname: String,
)
