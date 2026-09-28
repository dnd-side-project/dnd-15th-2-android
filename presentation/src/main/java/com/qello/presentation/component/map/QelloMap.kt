package com.qello.presentation.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotationGroupState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.mapbox.maps.coroutine.cameraChangedEvents
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorAccuracyRadiusChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object QelloMapDefaults {
    const val STYLE_URL = "mapbox://styles/qello-edp/cmt5o78uc006b01rk6wu90yln"
}

// Mapbox가 정확도를 아직 안 줬을 때 쓸 값(전형적인 GPS 정확도 수준)
private const val DEFAULT_ACCURACY_METERS = 20.0

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun QelloMap(
    initialCenter: Point,
    initialZoom: Double,
    modifier: Modifier = Modifier,
    showsUserLocation: Boolean = false,
    // 현재 위치에서 방위각 방향으로 뻗어나가는 부채꼴을 지도 위에 실제 지리 도형으로 그린다 (showsUserLocation이 true일 때만 의미 있음)
    showsDirectionCone: Boolean = false,
    onBearingChanged: ((bearingDegrees: Float) -> Unit)? = null,
    onLocationSnapshot: ((latitude: Double, longitude: Double, accuracyMeters: Double) -> Unit)? = null,
) {
    val currentOnBearingChanged = rememberUpdatedState(onBearingChanged)
    val currentOnLocationSnapshot = rememberUpdatedState(onLocationSnapshot)

    // 부채꼴은 화면 픽셀이 아니라 실제 위/경도로 그리므로, 지도가 움직이거나 회전해도 같이 따라온다.
    var currentLocation by remember { mutableStateOf<Point?>(null) }
    var bearingDegrees by remember { mutableStateOf<Float?>(null) }
    // 줌 레벨에 따라 같은 실제 거리가 화면에서 차지하는 픽셀 수가 달라지므로, 화면 기준 크기를 유지하려면 필요하다
    var metersPerPixel by remember { mutableStateOf<Double?>(null) }
    val coneScreenReachPx = with(LocalDensity.current) { CONE_SCREEN_REACH_DP.dp.toPx() }

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
    ) {
        if (showsUserLocation) {
            // 위치 권한이 있어야 파란 점(내 위치)이 표시된다
            MapEffect(Unit) { mapView ->
                mapView.location.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                    puckBearingEnabled = true
                    puckBearing = PuckBearing.HEADING
                    // 방향은 지도 위에 우리가 직접 그리는 부채꼴로 표시하므로, 퍽 자체엔 화살표를 안 그린다
                    locationPuck = createDefault2DPuck(withBearing = false)
                }

                val bearingListener = OnIndicatorBearingChangedListener { degrees ->
                    bearingDegrees = degrees.toFloat()
                    currentOnBearingChanged.value?.invoke(degrees.toFloat())
                }
                mapView.location.addOnIndicatorBearingChangedListener(bearingListener)

                fun publishMetersPerPixel() {
                    val point = currentLocation ?: return
                    metersPerPixel = mapView.mapboxMap.getMetersPerPixelAtLatitude(point.latitude())
                }

                // 정확도는 위치와 별도로 들어와서, 위치가 먼저 오면 정확도를 아직 모를 수 있다.
                // 위치를 알게 된 시점 자체는 확실하니, 정확도를 모르면 기본값으로 두고 바로 흘려보낸다.
                var latestAccuracyMeters: Double = DEFAULT_ACCURACY_METERS

                // 실제 위치를 처음 알게 된 시점에 한 번만 그리로 카메라를 옮긴다(그 뒤로는 자유롭게 이동/확대 가능)
                var hasCenteredOnFirstFix = false
                val positionListener = OnIndicatorPositionChangedListener { point ->
                    currentLocation = point
                    if (!hasCenteredOnFirstFix) {
                        hasCenteredOnFirstFix = true
                        mapView.mapboxMap.setCamera(CameraOptions.Builder().center(point).build())
                    }
                    publishMetersPerPixel()
                    currentOnLocationSnapshot.value?.invoke(point.latitude(), point.longitude(), latestAccuracyMeters)
                }
                mapView.location.addOnIndicatorPositionChangedListener(positionListener)

                val accuracyListener = OnIndicatorAccuracyRadiusChangedListener { accuracyMeters ->
                    latestAccuracyMeters = accuracyMeters
                }
                mapView.location.addOnIndicatorAccuracyRadiusChangedListener(accuracyListener)

                // 줌(확대/축소)이 바뀌면 같은 실제 거리의 화면 크기가 달라지므로 다시 계산한다.
                // 제스처 중에는 이 이벤트가 프레임마다 발생해서, 매번 부채꼴 전체를 다시 그리면 깜빡여 보이므로 빈도를 제한한다.
                val cameraChangeJob = launch {
                    mapView.mapboxMap.cameraChangedEvents.sample(1000).collect { publishMetersPerPixel() }
                }

                try {
                    awaitCancellation()
                } finally {
                    cameraChangeJob.cancel()
                    mapView.location.removeOnIndicatorBearingChangedListener(bearingListener)
                    mapView.location.removeOnIndicatorPositionChangedListener(positionListener)
                    mapView.location.removeOnIndicatorAccuracyRadiusChangedListener(accuracyListener)
                }
            }
        }

        if (showsDirectionCone) {
            val origin = currentLocation
            val bearing = bearingDegrees
            val distanceMeters = metersPerPixel?.times(coneScreenReachPx)
            if (origin != null && bearing != null && distanceMeters != null) {
                DirectionConeBands(origin = origin, bearingDegrees = bearing.toDouble(), distanceMeters = distanceMeters)
            }
        }
    }
}

// 시안의 넓은 부채꼴 방향 표시. 서버 8방향 구간과 같은 폭(45도)으로 그린다.
// 줌과 무관하게 화면에서 항상 이만큼(dp)은 뻗어 보이도록, 실제 거리는 줌에 맞춰 매번 다시 계산한다.
private const val CONE_SCREEN_REACH_DP = 400f
private const val CONE_HALF_ANGLE_DEGREES = 22.5
private const val CONE_BAND_COUNT = 32
private const val ARC_SAMPLE_COUNT = 12
// 현위치 점(Mapbox 기본 퍽, mapbox_user_icon.xml)과 같은 색으로 맞춘다. 색 하나로 불투명도만 옅어지게 한다.
private val CONE_COLOR = Color(0xFF4A90E2)

/**
 * 부채꼴 하나를 옅어지는 느낌을 흉내 내기 위해, 안쪽부터 바깥쪽까지 여러 겹의 고리 모양 도형으로 나눠 그린다.
 * 개별 PolygonAnnotation은 지도 스타일의 3D 조명(라이트 프리셋) 영향을 받아 색이 어둡게 보이므로,
 * 조명 영향을 끌 수 있는(fillEmissiveStrength) PolygonAnnotationGroup으로 한 번에 그린다.
 */
@Composable
private fun DirectionConeBands(origin: Point, bearingDegrees: Double, distanceMeters: Double) {
    val bands = (0 until CONE_BAND_COUNT).map { band ->
        val innerFraction = band.toDouble() / CONE_BAND_COUNT
        val outerFraction = (band + 1).toDouble() / CONE_BAND_COUNT
        val ring = coneBandRing(
            origin = origin,
            bearingDegrees = bearingDegrees,
            innerDistanceMeters = distanceMeters * innerFraction,
            outerDistanceMeters = distanceMeters * outerFraction,
        )
        val midFraction = ((innerFraction + outerFraction) / 2.0).toFloat()

        PolygonAnnotationOptions()
            .withPoints(listOf(ring))
            .withFillColor(CONE_COLOR.toArgb())
            .withFillOpacity((1f - midFraction).toDouble())
            // 기본 1px 테두리가 밴드 경계에서 겹쳐 보이는 걸 막는다
            .withFillOutlineColor(Color.Transparent.toArgb())
    }

    PolygonAnnotationGroup(
        annotations = bands,
        polygonAnnotationGroupState = remember {
            PolygonAnnotationGroupState()
        }.apply {
            // 조명(빛) 효과를 받지 않고 우리가 지정한 색 그대로 보이게 한다
            fillEmissiveStrength = 1.0
        },
    )
}

private fun coneBandRing(
    origin: Point,
    bearingDegrees: Double,
    innerDistanceMeters: Double,
    outerDistanceMeters: Double,
): List<Point> {
    val outerArc = (0..ARC_SAMPLE_COUNT).map { step ->
        val angle = bearingDegrees - CONE_HALF_ANGLE_DEGREES +
            (CONE_HALF_ANGLE_DEGREES * 2 * step / ARC_SAMPLE_COUNT)
        destinationPoint(origin, angle, outerDistanceMeters)
    }
    val innerArc = if (innerDistanceMeters <= 0.0) {
        listOf(origin)
    } else {
        (0..ARC_SAMPLE_COUNT).map { step ->
            val angle = bearingDegrees - CONE_HALF_ANGLE_DEGREES +
                (CONE_HALF_ANGLE_DEGREES * 2 * step / ARC_SAMPLE_COUNT)
            destinationPoint(origin, angle, innerDistanceMeters)
        }.reversed()
    }
    return outerArc + innerArc + outerArc.first()
}

private const val EARTH_RADIUS_METERS = 6_371_000.0

/** [origin]에서 [bearingDegrees] 방향으로 [distanceMeters]만큼 떨어진 지점(대권 거리 공식). */
private fun destinationPoint(origin: Point, bearingDegrees: Double, distanceMeters: Double): Point {
    val angularDistance = distanceMeters / EARTH_RADIUS_METERS
    val bearingRadians = Math.toRadians(bearingDegrees)
    val lat1 = Math.toRadians(origin.latitude())
    val lon1 = Math.toRadians(origin.longitude())

    val lat2 = asin(
        sin(lat1) * cos(angularDistance) + cos(lat1) * sin(angularDistance) * cos(bearingRadians),
    )
    val lon2 = lon1 + atan2(
        sin(bearingRadians) * sin(angularDistance) * cos(lat1),
        cos(angularDistance) - sin(lat1) * sin(lat2),
    )

    return Point.fromLngLat(Math.toDegrees(lon2), Math.toDegrees(lat2))
}
