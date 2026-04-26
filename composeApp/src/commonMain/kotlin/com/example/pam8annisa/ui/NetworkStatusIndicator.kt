package com.example.pam8annisa.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pam8annisa.platform.NetworkMonitor
import com.example.pam8annisa.platform.NetworkType
import org.koin.compose.koinInject

/**
 * Komponen jaringan dua bagian:
 * 1. Banner merah animasi saat offline
 * 2. Card info jaringan (tipe + operator/SSID) saat online
 */
@Composable
fun NetworkStatusIndicator(modifier: Modifier = Modifier) {
    val networkMonitor: NetworkMonitor = koinInject()
    val networkInfo by networkMonitor.observeNetworkInfo()
        .collectAsState(initial = networkMonitor.getNetworkInfo())

    Column(modifier = modifier) {

        // ── Banner offline ──────────────────────────────────────────────
        AnimatedVisibility(
            visible = !networkInfo.isConnected,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📡", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Tidak ada koneksi internet",
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // ── Card info jaringan (selalu tampil saat online) ──────────────
        AnimatedVisibility(
            visible = networkInfo.isConnected,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            val isWifi = networkInfo.type == NetworkType.WIFI
            val bgColor = if (isWifi) Color(0xFF1B5E20) else Color(0xFF0D47A1)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Kiri: ikon + tipe jaringan
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isWifi) "📶" else "📱",
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isWifi) "WiFi" else "Data Seluler",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                            Text(
                                text = networkInfo.operatorName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Kanan: sinyal (hanya WiFi)
                    if (isWifi && networkInfo.signalStrength != "-") {
                        val signalColor = when (networkInfo.signalStrength) {
                            "Kuat"   -> Color(0xFF69F0AE)
                            "Sedang" -> Color(0xFFFFD740)
                            else     -> Color(0xFFFF5252)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Sinyal",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = networkInfo.signalStrength,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = signalColor
                            )
                        }
                    }
                }
            }
        }
    }
}
