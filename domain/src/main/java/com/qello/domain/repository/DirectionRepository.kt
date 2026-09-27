package com.qello.domain.repository

interface DirectionRepository {
    suspend fun updatePresence(
        latitude: Double,
        longitude: Double,
        accuracyMeters: Double,
        receiveAllowed: Boolean = true,
    )
}
