package com.example.pam8annisa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform