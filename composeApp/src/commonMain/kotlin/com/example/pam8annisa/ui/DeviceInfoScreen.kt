package com.example.pam8annisa.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pam8annisa.platform.BatteryInfo
import com.example.pam8annisa.platform.DeviceInfo
import org.koin.compose.koinInject

@Composable
fun DeviceInfoScreen(modifier: Modifier = Modifier) {
    val deviceInfo: DeviceInfo = koinInject()
    val batteryInfo: BatteryInfo = koinInject()

    val deviceName = remember { deviceInfo.getDeviceName() }
    val osVersion = remember { deviceInfo.getOsVersion() }
    val appVersion = remember { deviceInfo.getAppVersion() }
    val isTablet = remember { deviceInfo.isTablet() }
    val batteryLevel = remember { batteryInfo.getBatteryLevel() }
    val isCharging = remember { batteryInfo.isCharging() }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Info Perangkat",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Grid 2 kolom: stat cards kecil
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                emoji = "📱",
                label = "Tipe",
                value = if (isTablet) "Tablet" else "Smartphone",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                emoji = "🔋",
                label = "Baterai",
                value = if (batteryLevel >= 0) "$batteryLevel%" else "N/A",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                emoji = "⚡",
                label = "Charging",
                value = if (isCharging) "Ya" else "Tidak",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                emoji = "📦",
                label = "Versi App",
                value = appVersion,
                modifier = Modifier.weight(1f)
            )
        }

        // Card detail perangkat
        InfoCard(title = "🖥 Detail Perangkat") {
            InfoRow(label = "Nama", value = deviceName)
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
            InfoRow(label = "OS", value = osVersion)
        }

        // Card tentang aplikasi
//        InfoCard(title = "📚 Tentang Aplikasi") {
//            InfoRow(label = "Mata Kuliah", value = "Pengembangan Aplikasi Mobile")
//            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
//            InfoRow(label = "Pertemuan", value = "8 – Platform Features")
//            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
//            InfoRow(label = "Teknologi", value = "Kotlin Multiplatform")
//        }
    }
}

@Composable
private fun StatCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.5f)
        )
    }
}
