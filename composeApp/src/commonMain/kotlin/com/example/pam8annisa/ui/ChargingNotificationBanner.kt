package com.example.pam8annisa.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pam8annisa.platform.BatteryInfo
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

/**
 * Banner notifikasi charging yang muncul HANYA saat status berubah.
 * Tidak muncul saat app pertama dibuka.
 * Auto-hide setelah 3 detik.
 */
@Composable
fun ChargingNotificationBanner(modifier: Modifier = Modifier) {
    val batteryInfo: BatteryInfo = koinInject()

    // Simpan status awal sebagai baseline — pakai remember agar tidak berubah saat recompose
    val initialCharging = remember { batteryInfo.isCharging() }
    val isCharging by batteryInfo.observeCharging().collectAsState(initial = initialCharging)

    var showBanner by remember { mutableStateOf(false) }
    var bannerMessage by remember { mutableStateOf("") }
    var isPluggedIn by remember { mutableStateOf(initialCharging) }

    // previousCharging menyimpan nilai sebelumnya untuk deteksi perubahan nyata
    var previousCharging by remember { mutableStateOf(initialCharging) }

    LaunchedEffect(isCharging) {
        // Hanya tampilkan banner jika ada perubahan dari nilai sebelumnya
        if (isCharging == previousCharging) return@LaunchedEffect

        previousCharging = isCharging
        isPluggedIn = isCharging
        bannerMessage = if (isCharging) "⚡ Sedang mengisi daya..." else "🔌 Charger dilepas"
        showBanner = true
        delay(3000)
        showBanner = false
    }

    AnimatedVisibility(
        visible = showBanner,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isPluggedIn) Color(0xFF2E7D32) else Color(0xFFB71C1C)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isPluggedIn) "🔋" else "📵",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = bannerMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
