package com.example.pam8annisa.platform

import platform.UIKit.UIDevice
import platform.UIKit.UIUserInterfaceIdiomPad

actual class DeviceInfo actual constructor() {

    actual fun getDeviceName(): String = UIDevice.currentDevice.name

    actual fun getOsVersion(): String {
        val systemName = UIDevice.currentDevice.systemName()
        val systemVersion = UIDevice.currentDevice.systemVersion
        return "$systemName $systemVersion"
    }

    actual fun getAppVersion(): String = "1.0"

    actual fun isTablet(): Boolean =
        UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad
}
