package com.example.pam8annisa.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

actual class NetworkMonitor actual constructor() {

    private val _connectivity = MutableStateFlow(true)
    private val _networkInfo = MutableStateFlow(
        NetworkInfo(true, NetworkType.WIFI, "WiFi", "Kuat")
    )

    actual fun isConnected(): Boolean = true

    actual fun getNetworkInfo(): NetworkInfo =
        NetworkInfo(true, NetworkType.WIFI, "WiFi", "Kuat")

    actual fun observeConnectivity(): Flow<Boolean> = _connectivity

    actual fun observeNetworkInfo(): Flow<NetworkInfo> = _networkInfo
}
