package com.qello.data.remote.datasource

import com.qello.data.remote.request.SubmitDirectionPostRequest
import com.qello.data.remote.request.UpdatePresenceRequest
import com.qello.data.remote.response.DirectionPostSubmissionResponse
import com.qello.data.remote.response.UpdatePresenceResponse

interface DirectionDataSource {
    suspend fun updatePresence(request: UpdatePresenceRequest): UpdatePresenceResponse

    suspend fun submitPost(idempotencyKey: String, request: SubmitDirectionPostRequest): DirectionPostSubmissionResponse
}
