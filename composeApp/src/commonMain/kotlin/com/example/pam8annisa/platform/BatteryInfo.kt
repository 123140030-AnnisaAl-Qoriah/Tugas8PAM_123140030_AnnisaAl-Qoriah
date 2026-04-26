package com.example.pam8annisa.platform

import kotlinx.coroutines.flow.Flow

/**
 * expect class untuk mendapatkan informasi baterai perangkat.
 * Mendukung observeCharging() agar notifikasi charging bisa real-time.
 */
expect class BatteryInfo() {
    fun getBatteryLevel(): Int       // 0-100
    fun isCharging(): Boolean
    fun observeCharging(): Flow<Boolean>  // emit true saat charging berubah
}
