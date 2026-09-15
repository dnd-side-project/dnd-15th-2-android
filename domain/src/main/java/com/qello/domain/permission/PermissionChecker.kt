package com.qello.domain.permission

interface PermissionChecker {
    fun isLocationPermissionGranted(): Boolean
    fun isNotificationPermissionGranted(): Boolean
}
