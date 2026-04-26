package com.example.pam8annisa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pam8annisa.di.appModules
import com.example.pam8annisa.ui.ChargingNotificationBanner
import com.example.pam8annisa.ui.DeviceInfoScreen
import com.example.pam8annisa.ui.NetworkStatusIndicator
import org.koin.compose.KoinApplication

private val AnnisaColorScheme = lightColorScheme(
    primary = Color(0xFF00796B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB2DFDB),
    onPrimaryContainer = Color(0xFF00251E),
    secondary = Color(0xFF26A69A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2F1),
    onSecondaryContainer = Color(0xFF00352F),
    background = Color(0xFFF0FAFA),
    onBackground = Color(0xFF1A1C1C),
    surface = Color(0xFFF0FAFA),
    onSurface = Color(0xFF1A1C1C),
    surfaceVariant = Color(0xFFDAEDE9),
    onSurfaceVariant = Color(0xFF3F5552),
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun App() {
    KoinApplication(application = { modules(appModules) }) {
        MaterialTheme(colorScheme = AnnisaColorScheme) {
            AppContent()
        }
    }
}

@Composable
private fun AppContent() {
    var showSettings by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding()
            ) {
                NetworkStatusIndicator(modifier = Modifier.fillMaxWidth())

                if (showSettings) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        OutlinedButton(
                            onClick = { showSettings = false },
                            modifier = Modifier.padding(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("← Kembali")
                        }
                        DeviceInfoScreen(modifier = Modifier.fillMaxWidth())
                    }
                } else {
                    MainScreen(onNavigateToSettings = { showSettings = true })
                }
            }

            // Charging banner melayang di atas — overlay, tidak menggeser layout
            ChargingNotificationBanner(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 56.dp)
            )
        }
    }
}

@Composable
private fun MainScreen(onNavigateToSettings: () -> Unit) {
    var showPlatformInfo by remember { mutableStateOf(false) }
    val greeting = remember { Greeting().greet() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // Header card bergaya teal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🌿", fontSize = 40.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "PAM8 – Annisa",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Kotlin Multiplatform",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { showPlatformInfo = !showPlatformInfo },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = if (showPlatformInfo) "🙈 Sembunyikan Info Platform" else "🔍 Tampilkan Info Platform",
                fontWeight = FontWeight.SemiBold
            )
        }

        AnimatedVisibility(showPlatformInfo) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = greeting,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("⚙️ Info Perangkat & Baterai", fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(
//                    text = "✅ Fitur yang Diimplementasikan",
//                    style = MaterialTheme.typography.titleSmall,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//                Spacer(Modifier.height(10.dp))
//                listOf(
//                    "🟢 Koin Dependency Injection",
//                    "🟢 DeviceInfo (expect/actual)",
//                    "🟢 NetworkMonitor (expect/actual)",
//                    "🟢 Network Status Banner",
//                    "🟢 BatteryInfo (BONUS)",
//                    "🟢 Charging Notification Real-time"
//                ).forEach {
//                    Text(
//                        text = it,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        modifier = Modifier.padding(vertical = 3.dp)
//                    )
//                }
//            }
        }
    }
}
