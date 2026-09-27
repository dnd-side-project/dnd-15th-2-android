package com.qello.domain.repository

import com.qello.domain.model.MediaAsset

interface MediaRepository {
    /** 이미지 하나를 업로드하고(자리 받기 → 실제 업로드 → 업로드 확인), 첨부 가능한 상태로 만든다. */
    suspend fun uploadImage(imageUri: String): MediaAsset
}
