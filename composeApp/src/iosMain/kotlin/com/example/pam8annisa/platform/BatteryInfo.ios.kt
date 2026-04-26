package com.example.pam8annisa.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceBatteryState

/**
 * iOS actual implementation dari BatteryInfo.
 * observeCharging() menggunakan MutableStateFlow yang dapat diperbarui
 * melalui UIDevice battery monitoring.
 */
actual class BatteryInfo actual constructor() {

    private val _chargingFlow = MutableStateFlow(false)

    init {
        UIDevice.currentDevice.batteryMonitoringEnabled = true
        _chargingFlow.value = isCharging()
    }

    actual fun getBatteryLevel(): Int {
        val level = UIDevice.currentDevice.batteryLevel
        return if (level < 0) -1 else (level * 100).toInt()
    }

    actual fun isCharging(): Boolean {
        val state = UIDevice.currentDevice.batteryState
        return state == UIDeviceBatteryState.UIDeviceBatteryStateCharging ||
                state == UIDeviceBatteryState.UIDeviceBatteryStateFull
    }

    actual fun observeCharging(): Flow<Boolean> {
        // Perbarui state saat ini
        _chargingFlow.value = isCharging()
        return _chargingFlow
    }
}
