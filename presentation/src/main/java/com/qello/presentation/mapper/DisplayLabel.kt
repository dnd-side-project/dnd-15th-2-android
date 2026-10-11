package com.qello.presentation.mapper

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val timeWithSecondsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

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

internal fun String.toLocalTimeLabel(): String =
    Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)

internal fun String.toPostedAtLabel(): String =
    Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalTime().format(timeWithSecondsFormatter)
