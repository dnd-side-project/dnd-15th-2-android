package com.qello.presentation.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle

object QelloMapDefaults {
    // Mapbox Studio > Share > Style URL
    const val STYLE_URL = "mapbox://styles/qello-edp/cmt5o78uc006b01rk6wu90yln"
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun QelloMap(
    initialCenter: Point,
    initialZoom: Double,
    modifier: Modifier = Modifier,
) {
    MapboxMap(
        modifier = modifier,
        scaleBar = {},
        logo = {},
        attribution = {},
        style = {
            MapStyle(style = QelloMapDefaults.STYLE_URL)
        },
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                center(initialCenter)
                zoom(initialZoom)
            }
        },
    )
}
