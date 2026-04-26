package com.example.pam8annisa.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(additionalConfig: KoinApplication.() -> Unit = {}) {
    startKoin {
        modules(appModules)
        additionalConfig()
    }
}
