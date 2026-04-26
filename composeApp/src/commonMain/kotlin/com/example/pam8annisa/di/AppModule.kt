package com.example.pam8annisa.di

import com.example.pam8annisa.platform.BatteryInfo
import com.example.pam8annisa.platform.DeviceInfo
import com.example.pam8annisa.platform.NetworkMonitor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    singleOf(::DeviceInfo)
    singleOf(::NetworkMonitor)
    singleOf(::BatteryInfo)
}

val appModules = listOf(commonModule)
