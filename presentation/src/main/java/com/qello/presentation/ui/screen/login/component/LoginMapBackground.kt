package com.qello.presentation.ui.screen.login.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.BooleanValue
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.rememberStandardStyleState

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun LoginMapBackground(modifier: Modifier = Modifier) {
    MapboxMap(
        modifier = modifier,
        style = {
            MapboxStandardStyle(
                standardStyleState = rememberStandardStyleState {
                    configurationsState.apply {
                        lightPreset = LightPresetValue.NIGHT
                        show3dObjects = BooleanValue(false)
                    }
                },
            )
        },
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                center(Point.fromLngLat(0.0, 20.0))
                zoom(1.0)
            }
        },
    )
}
