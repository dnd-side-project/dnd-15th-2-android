package com.qello.presentation.mapper

import java.time.Instant
import java.time.temporal.ChronoUnit

internal fun distanceLabel(distanceBand: String?, distanceM: Long?): String {
    distanceBand?.let { return it }
    val meters = distanceM ?: return ""
    return if (meters >= 1000) "${meters / 1000}km" else "${meters}m"
}

internal fun String.toRelativeTimeLabel(now: Instant): String {
    val minutes = ChronoUnit.MINUTES.between(Instant.parse(this), now).coerceAtLeast(0)
    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        minutes < 60 * 24 -> "${minutes / 60}시간 전"
        else -> "${minutes / (60 * 24)}일 전"
    }
}
