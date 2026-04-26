package com.example.pam8annisa.platform

/**
 * expect class untuk mendapatkan informasi perangkat.
 */
expect class DeviceInfo() {
    fun getDeviceName(): String
    fun getOsVersion(): String
    fun getAppVersion(): String
    fun isTablet(): Boolean
}
