package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary

@Composable
fun SettingsScreen(
    selectedLocation: String,
    onLocationChange: (String) -> Unit,
    onSignOutToggle: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var bookingAlertsEnabled by remember { mutableStateOf(true) }
    var cropTipsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("English") }

    val languages = listOf("English", "मराठी (Marathi)", "हिंदी (Hindi)")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AgriDarkGreen)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("Platform Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                Text("App preferences, notifications, regional language & account options", fontSize = 11.sp, color = AgriTextSecondary)
            }
        }

        // Notification Preferences
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Notification Preferences", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AgriTextPrimary)
                }

                HorizontalDivider(color = AgriBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Equipment Booking Approvals", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Text("Receive instant alerts when machinery requests are approved or scheduled", fontSize = 10.sp, color = AgriTextSecondary)
                    }
                    Switch(
                        checked = bookingAlertsEnabled,
                        onCheckedChange = { bookingAlertsEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AgriGreenPrimary)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Crop Weather & Disease Advisories", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Text("Periodic suggestions for pest outbreaks and optimal fertigation", fontSize = 10.sp, color = AgriTextSecondary)
                    }
                    Switch(
                        checked = cropTipsEnabled,
                        onCheckedChange = { cropTipsEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AgriGreenPrimary)
                    )
                }
            }
        }

        // Language & Localization
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Regional Language", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AgriTextPrimary)
                }

                HorizontalDivider(color = AgriBorder)

                languages.forEach { lang ->
                    val isSelected = selectedLanguage == lang
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFE8F5E9) else Color.Transparent)
                            .clickable { selectedLanguage = lang }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(lang, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        if (isSelected) {
                            Text("Active", fontSize = 10.sp, color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Account & Security
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Privacy & Security", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AgriTextPrimary)
                }

                HorizontalDivider(color = AgriBorder)

                Text(
                    text = "Your data is securely stored locally on device. Farmer phone numbers and field coordinates are shared only with confirmed equipment owners.",
                    fontSize = 11.sp,
                    color = AgriTextSecondary,
                    lineHeight = 15.sp
                )

                OutlinedButton(
                    onClick = onSignOutToggle,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("sign_out_button")
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sign Out / Switch Profile", fontSize = 12.sp, color = Color(0xFFDC2626))
                }
            }
        }
    }
}
