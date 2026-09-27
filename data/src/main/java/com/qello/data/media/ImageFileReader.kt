package com.qello.data.media

import android.content.Context
import android.net.Uri
import com.qello.data.di.Dispatcher
import com.qello.data.di.QelloDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import javax.inject.Inject

class ImageFileContent(
    val bytes: ByteArray,
    val contentType: String,
    val checksum: String,
)

/** 갤러리에서 고른 이미지(content:// Uri)를 업로드에 필요한 형태로 읽어온다. */
class ImageFileReader @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(QelloDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun read(imageUri: String): ImageFileContent = withContext(ioDispatcher) {
        val uri = Uri.parse(imageUri)
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: error("이미지를 읽을 수 없습니다: $imageUri")
        val contentType = context.contentResolver.getType(uri) ?: DEFAULT_CONTENT_TYPE

        ImageFileContent(
            bytes = bytes,
            contentType = contentType,
            checksum = bytes.toMd5Hex(),
        )
    }

    private fun ByteArray.toMd5Hex(): String =
        MessageDigest.getInstance("MD5").digest(this).joinToString(separator = "") { "%02x".format(it) }

    private companion object {
        const val DEFAULT_CONTENT_TYPE = "image/jpeg"
    }
}
