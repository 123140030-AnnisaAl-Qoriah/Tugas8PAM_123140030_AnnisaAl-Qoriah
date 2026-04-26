package com.example.pam8annisa.platform

import kotlinx.coroutines.flow.Flow

/**
 * Data class untuk menyimpan info lengkap jaringan.
 */
data class NetworkInfo(
    val isConnected: Boolean,
    val type: NetworkType,
    val operatorName: String,   // Nama operator seluler / nama WiFi SSID
    val signalStrength: String  // "Kuat" / "Sedang" / "Lemah" / "-"
)

enum class NetworkType {
    WIFI,
    CELLULAR,
    NONE
}

/**
 * expect class untuk memonitor status koneksi jaringan secara lengkap:
 * - Status koneksi (online/offline)
 * - Tipe jaringan (WiFi / Seluler)
 * - Nama operator / SSID
 */
expect class NetworkMonitor() {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
    fun getNetworkInfo(): NetworkInfo
    fun observeNetworkInfo(): Flow<NetworkInfo>
}
