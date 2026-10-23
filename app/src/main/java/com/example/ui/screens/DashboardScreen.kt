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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.data.model.Equipment
import com.example.data.model.PlantScan
import com.example.data.model.SoilReport
import com.example.data.model.UserProfile
import com.example.ui.ScreenDestination
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenAccent
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextMuted
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary
import com.example.ui.theme.StatusApprovedGreen
import com.example.ui.theme.StatusPendingOrange
import com.example.ui.theme.StatusRejectedRed

@Composable
fun DashboardScreen(
    userProfile: UserProfile?,
    allEquipment: List<Equipment>,
    allBookings: List<Booking>,
    allPlantScans: List<PlantScan>,
    allSoilReports: List<SoilReport>,
    activeRole: String,
    onRoleChange: (String) -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val roles = listOf("Farmer", "Equipment Owner", "Admin")
    var selectedRoleIndex by remember {
        mutableIntStateOf(roles.indexOf(activeRole).coerceAtLeast(0))
    }

    val activeBookingsCount = allBookings.count { it.status == BookingStatus.APPROVED }
    val pendingBookingsCount = allBookings.count { it.status == BookingStatus.PENDING }
    val completedBookingsCount = allBookings.count { it.status == BookingStatus.COMPLETED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Title & Role Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Farming Dashboard",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriDarkGreen
                )
                Text(
                    text = "Live overview of bookings, equipment, and crop health",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )
            }

            // Role Pills
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(1.dp, AgriBorder, RoundedCornerShape(20.dp))
                    .padding(3.dp)
            ) {
                roles.forEachIndexed { idx, role ->
                    val isSelected = selectedRoleIndex == idx
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) AgriDarkGreen else Color.Transparent)
                            .clickable {
                                selectedRoleIndex = idx
                                onRoleChange(role)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("role_pill_$role")
                    ) {
                        Text(
                            text = role,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else AgriTextSecondary
                        )
                    }
                }
            }
        }

        // Metrics Grid (6 Metric Cards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardMetricCard(
                title = "Available Equipment",
                value = "${allEquipment.count { it.isAvailable }}",
                subtitle = "In your district",
                icon = Icons.Default.Agriculture,
                color = Color(0xFF2563EB),
                bgColor = Color(0xFFEBF5FF),
                modifier = Modifier.weight(1f)
            )
            DashboardMetricCard(
                title = "Active Bookings",
                value = "$activeBookingsCount",
                subtitle = "Confirmed rentals",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF16A34A),
                bgColor = Color(0xFFDCFCE7),
                modifier = Modifier.weight(1f)
            )
            DashboardMetricCard(
                title = "Pending Requests",
                value = "$pendingBookingsCount",
                subtitle = "Awaiting confirmation",
                icon = Icons.Default.PendingActions,
                color = Color(0xFFD97706),
                bgColor = Color(0xFFFEF3C7),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardMetricCard(
                title = "Completed Bookings",
                value = "$completedBookingsCount",
                subtitle = "Rental history",
                icon = Icons.Default.DateRange,
                color = Color(0xFF7C3AED),
                bgColor = Color(0xFFF3E8FF),
                modifier = Modifier.weight(1f)
            )
            DashboardMetricCard(
                title = "Plant Scans",
                value = "${allPlantScans.size}",
                subtitle = "Health analyses",
                icon = Icons.Default.CropFree,
                color = Color(0xFF00897B),
                bgColor = Color(0xFFE0F2F1),
                modifier = Modifier.weight(1f)
            )
            DashboardMetricCard(
                title = "Saved Reports",
                value = "${allPlantScans.size + allSoilReports.size}",
                subtitle = "Scans & soil tests",
                icon = Icons.Default.Description,
                color = Color(0xFFE11D48),
                bgColor = Color(0xFFFFE4E6),
                modifier = Modifier.weight(1f)
            )
        }

        // Role-Specific Section
        when (roles[selectedRoleIndex]) {
            "Equipment Owner" -> OwnerDashboardSection(
                equipment = allEquipment,
                bookings = allBookings,
                onAddListing = { onNavigate(ScreenDestination.EQUIPMENT_BOOKING) }
            )
            "Admin" -> AdminDashboardSection(
                equipmentCount = allEquipment.size,
                bookingsCount = allBookings.size,
                reportsCount = allPlantScans.size + allSoilReports.size
            )
            else -> FarmerDashboardSection(
                bookings = allBookings,
                plantScans = allPlantScans,
                onDiscoverEquipment = { onNavigate(ScreenDestination.EQUIPMENT_BOOKING) },
                onScanCrop = { onNavigate(ScreenDestination.SCANNER) }
            )
        }
    }
}

@Composable
fun DashboardMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }

                Text(
                    text = value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriDarkGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgriTextPrimary,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = AgriTextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun FarmerDashboardSection(
    bookings: List<Booking>,
    plantScans: List<PlantScan>,
    onDiscoverEquipment: () -> Unit,
    onScanCrop: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AgriBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Equipment Bookings & Actions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onDiscoverEquipment,
                        colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Find Equipment", fontSize = 11.sp, color = Color.White)
                    }

                    Button(
                        onClick = onScanCrop,
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Scan Crop", fontSize = 11.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = AgriBorder)
            Spacer(modifier = Modifier.height(14.dp))

            if (bookings.isEmpty()) {
                Text(
                    text = "No active bookings yet. Browse featured machinery to rent for your farm.",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )
            } else {
                bookings.forEach { b ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(b.equipmentTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AgriTextPrimary)
                            Text("Duration: ${b.durationDays} days • Total: ₹${b.totalCost}", fontSize = 11.sp, color = AgriTextSecondary)
                            Text("Booked for: ${b.startDate}", fontSize = 10.sp, color = AgriTextMuted)
                        }

                        val (statusText, statusBg, statusColor) = when (b.status) {
                            BookingStatus.APPROVED -> Triple("Approved", Color(0xFFDCFCE7), StatusApprovedGreen)
                            BookingStatus.PENDING -> Triple("Pending", Color(0xFFFEF3C7), StatusPendingOrange)
                            BookingStatus.COMPLETED -> Triple("Completed", Color(0xFFE0E7FF), Color(0xFF4338CA))
                            BookingStatus.CANCELLED -> Triple("Cancelled", Color(0xFFF3F4F6), Color(0xFF6B7280))
                            BookingStatus.REJECTED -> Triple("Rejected", Color(0xFFFFE4E6), StatusRejectedRed)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(statusBg)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(statusText, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                }
            }
        }
    }
}

@Composable
fun OwnerDashboardSection(
    equipment: List<Equipment>,
    bookings: List<Booking>,
    onAddListing: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AgriBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Equipment Owner Operations",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )

                Button(
                    onClick = onAddListing,
                    colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("+ Add Equipment", fontSize = 11.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Your listed machinery is currently generating rental revenue. You can manage availability and review tenant farmer requests.",
                fontSize = 12.sp,
                color = AgriTextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            equipment.forEach { eq ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(eq.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AgriTextPrimary)
                        Text("Rate: ₹${eq.dailyRate}/day • Rating: ${eq.rating} ★", fontSize = 11.sp, color = AgriTextSecondary)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (eq.isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEE2E2))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (eq.isAvailable) "Available" else "Rented Out",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (eq.isAvailable) StatusApprovedGreen else StatusRejectedRed
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDashboardSection(
    equipmentCount: Int,
    bookingsCount: Int,
    reportsCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AgriBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Platform Administration Summary",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AgriTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Platform status: Operational. Regional Maharashtra equipment clusters: Pune (2 listings), Nashik (1 listing), Satara (1 listing).",
                fontSize = 12.sp,
                color = AgriTextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$equipmentCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                    Text("Total Listings", fontSize = 11.sp, color = AgriTextSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$bookingsCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                    Text("Total Bookings", fontSize = 11.sp, color = AgriTextSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$reportsCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7C3AED))
                    Text("Reports Generated", fontSize = 11.sp, color = AgriTextSecondary)
                }
            }
        }
    }
}
