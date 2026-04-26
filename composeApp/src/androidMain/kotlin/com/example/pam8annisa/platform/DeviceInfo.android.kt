package com.example.pam8annisa.platform

import android.content.Context
import android.os.Build
import kotlin.math.pow
import kotlin.math.sqrt

actual class DeviceInfo actual constructor() {

    private val context: Context
        get() = DeviceInfoContextHolder.context
            ?: error("DeviceInfo: Context belum di-set.")

    actual fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        val model = Build.MODEL
        return if (model.startsWith(manufacturer, ignoreCase = true)) model
        else "$manufacturer $model"
    }

    actual fun getOsVersion(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    actual fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    actual fun isTablet(): Boolean {
        val metrics = context.resources.displayMetrics
        val diagonalInches = sqrt(
            (metrics.widthPixels / metrics.xdpi).pow(2) +
                    (metrics.heightPixels / metrics.ydpi).pow(2)
        )
        return diagonalInches >= 7.0
    }
}

object DeviceInfoContextHolder {
    var context: Context? = null
}
