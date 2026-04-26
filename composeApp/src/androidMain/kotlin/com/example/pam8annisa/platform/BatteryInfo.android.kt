package com.example.pam8annisa.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Android actual implementation dari BatteryInfo.
 * observeCharging() menggunakan BroadcastReceiver sehingga notifikasi
 * muncul secara instan begitu charger disambungkan atau dilepas.
 */
actual class BatteryInfo actual constructor() {

    private val context: Context
        get() = BatteryInfoContextHolder.context
            ?: error("BatteryInfo: Context belum di-set.")

    private val batteryManager: BatteryManager
        get() = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    actual fun getBatteryLevel(): Int {
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    actual fun isCharging(): Boolean {
        val intent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
    }

    /**
     * Menggunakan BroadcastReceiver untuk mendengarkan event:
     * - ACTION_POWER_CONNECTED  → charger disambungkan
     * - ACTION_POWER_DISCONNECTED → charger dilepas
     *
     * Lebih responsif dari polling karena dipicu langsung oleh sistem.
     */
    actual fun observeCharging(): Flow<Boolean> = callbackFlow {
        // Emit status awal
        trySend(isCharging())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_POWER_CONNECTED    -> trySend(true)
                    Intent.ACTION_POWER_DISCONNECTED -> trySend(false)
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }

        context.registerReceiver(receiver, filter)

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }.distinctUntilChanged()
}

object BatteryInfoContextHolder {
    var context: Context? = null
}
