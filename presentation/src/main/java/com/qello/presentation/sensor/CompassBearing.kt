package com.qello.presentation.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val UPRIGHT_THRESHOLD = 0.7f
private const val SMOOTHING_FACTOR = 0.3f
private const val DEFAULT_INTERVAL_MILLIS = 200L
private const val NANOS_PER_MILLI = 1_000_000L
private const val MICROS_PER_MILLI = 1_000

/** 방위각(0~360도, 북=0, 시계방향, 자북 기준). 센서가 없거나 아직 값을 못 받았으면 null. */
@Composable
fun rememberCompassBearing(intervalMillis: Long = DEFAULT_INTERVAL_MILLIS): State<Float?> {
    val context = LocalContext.current
    val bearing = remember { mutableStateOf<Float?>(null) }

    LifecycleResumeEffect(Unit) {
        val sensorManager = context.getSystemService(SensorManager::class.java)
        val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val listener = object : SensorEventListener {
            private val rotationMatrix = FloatArray(9)
            private val remappedMatrix = FloatArray(9)
            private val orientation = FloatArray(3)
            private var sinAverage = 0f
            private var cosAverage = 0f
            private var hasAverage = false
            private var lastPublishedNanos = 0L

            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                val isUpright = abs(rotationMatrix[8]) < UPRIGHT_THRESHOLD
                val matrix = if (isUpright) {
                    SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedMatrix,
                    )
                    remappedMatrix
                } else {
                    rotationMatrix
                }
                SensorManager.getOrientation(matrix, orientation)

                // 0/360도 경계에서 튀지 않도록 각도를 sin/cos로 평균낸다
                val azimuth = orientation[0]
                if (hasAverage) {
                    sinAverage += (sin(azimuth) - sinAverage) * SMOOTHING_FACTOR
                    cosAverage += (cos(azimuth) - cosAverage) * SMOOTHING_FACTOR
                } else {
                    sinAverage = sin(azimuth)
                    cosAverage = cos(azimuth)
                    hasAverage = true
                }

                // 센서가 더 자주 들어와도 지정한 간격마다 한 번만 값을 내보낸다
                val isFirst = lastPublishedNanos == 0L
                if (isFirst || event.timestamp - lastPublishedNanos >= intervalMillis * NANOS_PER_MILLI) {
                    lastPublishedNanos = event.timestamp
                    val degrees = Math.toDegrees(atan2(sinAverage, cosAverage).toDouble()).toFloat()
                    bearing.value = (degrees + 360f) % 360f
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        if (rotationSensor != null) {
            sensorManager.registerListener(listener, rotationSensor, (intervalMillis * MICROS_PER_MILLI).toInt())
        }

        onPauseOrDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    return bearing
}
