package com.example.pam8annisa.platform

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

actual class NetworkMonitor actual constructor() {

    private val context: Context
        get() = NetworkMonitorContextHolder.context
            ?: error("NetworkMonitor: Context belum di-set.")

    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    actual fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    actual fun getNetworkInfo(): NetworkInfo {
        val network = connectivityManager.activeNetwork
            ?: return NetworkInfo(false, NetworkType.NONE, "-", "-")
        val capabilities = connectivityManager.getNetworkCapabilities(network)
            ?: return NetworkInfo(false, NetworkType.NONE, "-", "-")

        val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                val wifiManager = context.applicationContext
                    .getSystemService(Context.WIFI_SERVICE) as WifiManager
                @Suppress("DEPRECATION")
                val ssid = wifiManager.connectionInfo?.ssid
                    ?.removeSurrounding("\"") ?: "WiFi"
                val rssi = wifiManager.connectionInfo?.rssi ?: -100
                val strength = when {
                    rssi >= -60 -> "Kuat"
                    rssi >= -75 -> "Sedang"
                    else -> "Lemah"
                }
                NetworkInfo(isConnected, NetworkType.WIFI, "WiFi: $ssid", strength)
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val operator = telephony.networkOperatorName.ifEmpty { "Seluler" }
                val networkGen = when (telephony.networkType) {
                    TelephonyManager.NETWORK_TYPE_LTE -> "4G LTE"
                    TelephonyManager.NETWORK_TYPE_NR  -> "5G"
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA -> "3G"
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_GPRS -> "2G"
                    else -> "Seluler"
                }
                NetworkInfo(isConnected, NetworkType.CELLULAR, "$operator ($networkGen)", "–")
            }
            else -> NetworkInfo(isConnected, NetworkType.NONE, "-", "-")
        }
    }

    actual fun observeConnectivity(): Flow<Boolean> = callbackFlow {
        trySend(isConnected())
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { trySend(true) }
            override fun onLost(network: Network) { trySend(false) }
            override fun onCapabilitiesChanged(network: Network, cap: NetworkCapabilities) {
                trySend(cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
            }
        }
        connectivityManager.registerNetworkCallback(request, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    actual fun observeNetworkInfo(): Flow<NetworkInfo> = callbackFlow {
        trySend(getNetworkInfo())
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { trySend(getNetworkInfo()) }
            override fun onLost(network: Network) {
                trySend(NetworkInfo(false, NetworkType.NONE, "-", "-"))
            }
            override fun onCapabilitiesChanged(network: Network, cap: NetworkCapabilities) {
                trySend(getNetworkInfo())
            }
        }
        connectivityManager.registerNetworkCallback(request, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}

object NetworkMonitorContextHolder {
    var context: Context? = null
}
