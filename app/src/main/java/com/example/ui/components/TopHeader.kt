package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.NotificationItem
import com.example.data.model.UserProfile
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextMuted
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary

@Composable
fun TopHeader(
    userProfile: UserProfile?,
    selectedLocation: String,
    notifications: List<NotificationItem>,
    onLocationChange: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onOpenDrawer: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var locationMenuExpanded by remember { mutableStateOf(false) }
    var notificationMenuExpanded by remember { mutableStateOf(false) }
    var profileMenuExpanded by remember { mutableStateOf(false) }

    val locationsList = listOf(
        "Pune, Maharashtra",
        "Nashik, Maharashtra",
        "Satara, Maharashtra",
        "Kolhapur, Maharashtra",
        "Ahmednagar, Maharashtra"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        val isCompact = maxWidth < 600.dp

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side
            if (onOpenDrawer != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier.testTag("menu_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu",
                            tint = AgriTextPrimary
                        )
                    }
                    if (isCompact) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_agrishare_logo),
                            contentDescription = "AgriShare",
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AgriShare",
                            color = AgriGreenPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.width(4.dp))
            }

            // Right Controls: Location, Notification Bell, User Avatar & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 16.dp)
            ) {
                // Location dropdown
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { locationMenuExpanded = true }
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .testTag("location_selector")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isCompact) selectedLocation.split(",").firstOrNull() ?: "Pune" else selectedLocation,
                            color = AgriTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Location",
                            tint = AgriTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = locationMenuExpanded,
                        onDismissRequest = { locationMenuExpanded = false }
                    ) {
                        locationsList.forEach { loc ->
                            DropdownMenuItem(
                                text = { Text(loc, fontSize = 13.sp) },
                                onClick = {
                                    onLocationChange(loc)
                                    locationMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Notification Bell with badge
                Box {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6))
                            .clickable { notificationMenuExpanded = true }
                            .testTag("notifications_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = AgriTextPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                        // Unread badge "1"
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = notificationMenuExpanded,
                        onDismissRequest = { notificationMenuExpanded = false }
                    ) {
                        notifications.take(3).forEach { notif ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(notif.title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                        Text(notif.subtitle, color = AgriTextSecondary, fontSize = 11.sp)
                                        Text(notif.timeAgo, color = AgriTextMuted, fontSize = 10.sp)
                                    }
                                },
                                onClick = { notificationMenuExpanded = false }
                            )
                        }
                    }
                }

                // User Profile Chip
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { profileMenuExpanded = true }
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                            .testTag("user_profile_chip")
                    ) {
                        // Profile Avatar
                        Image(
                            painter = painterResource(id = R.drawable.user_avatar),
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color(0xFFE5E7EB), CircleShape)
                        )
                        if (!isCompact) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                val displayName = if (userProfile?.isLoggedIn == true && userProfile.fullName.isNotBlank()) {
                                    userProfile.fullName
                                } else {
                                    "AgriShare User"
                                }
                                val displayRole = userProfile?.role ?: "Student"

                                Text(
                                    text = displayName,
                                    color = AgriTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = displayRole,
                                    color = AgriTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "User options",
                            tint = AgriTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = profileMenuExpanded,
                        onDismissRequest = { profileMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("View Profile", fontSize = 13.sp) },
                            onClick = {
                                profileMenuExpanded = false
                                onNavigateToProfile()
                            }
                        )
                    }
                }
            }
        }
    }
}
