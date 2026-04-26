package com.example.pam8annisa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pam8annisa.platform.BatteryInfoContextHolder
import com.example.pam8annisa.platform.DeviceInfoContextHolder
import com.example.pam8annisa.platform.NetworkMonitorContextHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Set context untuk semua platform-specific class
        DeviceInfoContextHolder.context = applicationContext
        NetworkMonitorContextHolder.context = applicationContext
        BatteryInfoContextHolder.context = applicationContext

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
