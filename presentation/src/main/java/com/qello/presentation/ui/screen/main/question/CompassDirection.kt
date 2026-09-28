package com.qello.presentation.ui.screen.main.question

import androidx.annotation.StringRes
import com.qello.presentation.R

/** 서버의 OCTANT 방향 체계와 같은 순서(북부터 시계방향)·같은 키를 쓴다. */
enum class CompassDirection(
    val segmentKey: String,
    @StringRes val labelRes: Int,
) {
    NORTH("N", R.string.direction_north),
    NORTH_EAST("NE", R.string.direction_north_east),
    EAST("E", R.string.direction_east),
    SOUTH_EAST("SE", R.string.direction_south_east),
    SOUTH("S", R.string.direction_south),
    SOUTH_WEST("SW", R.string.direction_south_west),
    WEST("W", R.string.direction_west),
    NORTH_WEST("NW", R.string.direction_north_west),
    ;

    companion object {
        private const val SEGMENT_WIDTH_DEGREES = 45f

        /** 북(0도)을 중심으로 ±22.5도씩 8구간으로 나눈다. */
        fun fromBearing(bearingDegrees: Float): CompassDirection {
            val normalized = ((bearingDegrees % 360f) + 360f) % 360f
            val index = ((normalized + SEGMENT_WIDTH_DEGREES / 2) / SEGMENT_WIDTH_DEGREES).toInt() % entries.size
            return entries[index]
        }
    }
}
