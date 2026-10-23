package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.data.model.Equipment
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenAccent
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextMuted
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary
import com.example.ui.theme.RatingGold
import com.example.ui.theme.StatusApprovedGreen
import com.example.ui.theme.StatusPendingOrange
import com.example.ui.theme.StatusRejectedRed

@Composable
fun EquipmentBookingScreen(
    equipmentList: List<Equipment>,
    myBookings: List<Booking>,
    onSelectEquipment: (Equipment) -> Unit,
    onCancelBooking: (String) -> Unit,
    onUpdateBookingStatus: (bookingId: String, status: BookingStatus) -> Unit,
    onAddListing: (title: String, category: String, hp: String, dailyRate: Int, location: String, description: String) -> Unit,
    onToggleAvailability: (equipmentId: String, available: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Find Equipment", "My Bookings", "Equipment Owner")

    // Filter states for Find Equipment
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Tractor", "Tillage", "Harvesting", "Seeding")

    // Add listing dialog state
    var showAddListingModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
    ) {
        // Top Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = AgriDarkGreen,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AgriDarkGreen
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == index) AgriDarkGreen else AgriTextSecondary
                        )
                    },
                    modifier = Modifier.testTag("eq_tab_$index")
                )
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            when (selectedTab) {
                0 -> FindEquipmentTab(
                    equipmentList = equipmentList,
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    categories = categories,
                    onSearchChange = { searchQuery = it },
                    onCategoryChange = { selectedCategory = it },
                    onSelectEquipment = onSelectEquipment
                )
                1 -> MyBookingsTab(
                    bookings = myBookings,
                    onCancelBooking = onCancelBooking
                )
                2 -> EquipmentOwnerTab(
                    equipmentList = equipmentList,
                    bookings = myBookings,
                    onAddListingClick = { showAddListingModal = true },
                    onToggleAvailability = onToggleAvailability,
                    onUpdateBookingStatus = onUpdateBookingStatus
                )
            }
        }
    }

    if (showAddListingModal) {
        AddListingModal(
            onDismiss = { showAddListingModal = false },
            onConfirm = { title, category, hp, rate, loc, desc ->
                onAddListing(title, category, hp, rate, loc, desc)
                showAddListingModal = false
            }
        )
    }
}

@Composable
fun FindEquipmentTab(
    equipmentList: List<Equipment>,
    searchQuery: String,
    selectedCategory: String,
    categories: List<String>,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSelectEquipment: (Equipment) -> Unit
) {
    val filtered = equipmentList.filter { eq ->
        val matchesSearch = searchQuery.isBlank() ||
                eq.title.contains(searchQuery, ignoreCase = true) ||
                eq.location.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || eq.category.equals(selectedCategory, ignoreCase = true)
        matchesSearch && matchesCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search & Filter Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search machinery by name, power, or location...", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = AgriTextSecondary, modifier = Modifier.size(18.dp))
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("equipment_tab_search_input")
            )
        }

        // Category Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) AgriDarkGreen else Color.White)
                        .border(1.dp, if (isSelected) AgriDarkGreen else AgriBorder, RoundedCornerShape(16.dp))
                        .clickable { onCategoryChange(cat) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = cat,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else AgriTextPrimary
                    )
                }
            }
        }

        Text(
            text = "Showing ${filtered.size} verified farming implements",
            fontSize = 12.sp,
            color = AgriTextSecondary
        )

        // Equipment items list
        filtered.forEach { eq ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, AgriBorder, RoundedCornerShape(14.dp))
                    .clickable { onSelectEquipment(eq) },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imageRes = when (eq.imageDrawableName) {
                        "equip_rotavator" -> R.drawable.equip_rotavator
                        "equip_harvester" -> R.drawable.equip_harvester
                        "equip_seed_drill" -> R.drawable.equip_seed_drill
                        else -> R.drawable.equip_tractor
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = eq.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(eq.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AgriTextPrimary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (eq.isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEE2E2))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (eq.isAvailable) "Available" else "Rented",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (eq.isAvailable) StatusApprovedGreen else StatusRejectedRed
                                )
                            }
                        }

                        Text("₹${eq.dailyRate} / day", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = AgriDarkGreen)
                        Text(eq.description, fontSize = 11.sp, color = AgriTextSecondary, maxLines = 2, lineHeight = 14.sp)

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgriTextSecondary, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${eq.location} • ${eq.distanceKm} km", fontSize = 10.sp, color = AgriTextSecondary)
                            }

                            Button(
                                onClick = { onSelectEquipment(eq) },
                                colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text("Rent Now", fontSize = 10.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyBookingsTab(
    bookings: List<Booking>,
    onCancelBooking: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "My Equipment Bookings (${bookings.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AgriTextPrimary
        )

        if (bookings.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = AgriTextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No bookings made yet", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Find nearby tractors or rotavators in the Find Equipment tab.", fontSize = 11.sp, color = AgriTextSecondary)
                }
            }
        } else {
            bookings.forEach { b ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, AgriBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(b.equipmentTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AgriTextPrimary)
                                Text("Booking Ref: #${b.id.takeLast(6)}", fontSize = 10.sp, color = AgriTextMuted)
                            }

                            val (statusText, statusBg, statusColor) = when (b.status) {
                                BookingStatus.APPROVED -> Triple("Approved", Color(0xFFDCFCE7), StatusApprovedGreen)
                                BookingStatus.PENDING -> Triple("Pending Approval", Color(0xFFFEF3C7), StatusPendingOrange)
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

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = AgriBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Dates: ${b.startDate} (${b.durationDays} days)", fontSize = 11.sp, color = AgriTextSecondary)
                                Text("Location: ${b.location}", fontSize = 11.sp, color = AgriTextSecondary)
                            }
                            Text("Total: ₹${b.totalCost}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriDarkGreen)
                        }

                        if (b.status == BookingStatus.PENDING || b.status == BookingStatus.APPROVED) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = { onCancelBooking(b.id) },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Cancel Booking", fontSize = 10.sp, color = StatusRejectedRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EquipmentOwnerTab(
    equipmentList: List<Equipment>,
    bookings: List<Booking>,
    onAddListingClick: () -> Unit,
    onToggleAvailability: (equipmentId: String, available: Boolean) -> Unit,
    onUpdateBookingStatus: (bookingId: String, status: BookingStatus) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Machinery Owner Panel", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AgriTextPrimary)
                Text("Manage your equipment inventory and review incoming booking requests", fontSize = 11.sp, color = AgriTextSecondary)
            }

            Button(
                onClick = onAddListingClick,
                colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_listing_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Listing", fontSize = 11.sp, color = Color.White)
            }
        }

        // Pending Inquiries
        val pending = bookings.filter { it.status == BookingStatus.PENDING }
        if (pending.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF59E0B)))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Incoming Booking Requests (${pending.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF92400E))
                    Spacer(modifier = Modifier.height(8.dp))
                    pending.forEach { req ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("${req.farmerName} requested ${req.equipmentTitle}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Text("${req.durationDays} days • Total: ₹${req.totalCost}", fontSize = 10.sp, color = Color(0xFF78350F))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { onUpdateBookingStatus(req.id, BookingStatus.APPROVED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("Approve", fontSize = 10.sp, color = Color.White)
                                }
                                OutlinedButton(
                                    onClick = { onUpdateBookingStatus(req.id, BookingStatus.REJECTED) },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("Decline", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // My Machinery Inventory
        Text("My Listed Equipment", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriTextPrimary)

        equipmentList.forEach { eq ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, AgriBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(eq.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriTextPrimary)
                        Text("Rental: ₹${eq.dailyRate}/day • ${eq.location}", fontSize = 11.sp, color = AgriTextSecondary)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (eq.isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEE2E2))
                                .clickable { onToggleAvailability(eq.id, !eq.isAvailable) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (eq.isAvailable) "Available" else "Busy / In-Use",
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
}

@Composable
fun AddListingModal(
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: String, hp: String, rate: Int, location: String, description: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Tractor") }
    var hp by remember { mutableStateOf("50 HP") }
    var rateText by remember { mutableStateOf("1000") }
    var location by remember { mutableStateOf("Pune, Maharashtra") }
    var description by remember { mutableStateOf("Excellent working condition with serviced hydraulic hitch.") }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("List New Farm Equipment", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AgriDarkGreen)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Equipment Name (e.g. Tractor 50 HP)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (Tractor, Tillage, Harvesting, Seeding)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hp,
                    onValueChange = { hp = it },
                    label = { Text("Power / Specifications (e.g. 50 HP, 7 Feet)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = rateText,
                    onValueChange = { rateText = it },
                    label = { Text("Daily Rental Rate (₹ / day)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location (District, State)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Implements included") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val rate = rateText.toIntOrNull() ?: 1000
                            onConfirm(
                                if (title.isBlank()) "Heavy Farm Machinery" else title,
                                category,
                                hp,
                                rate,
                                location,
                                description
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Listing", color = Color.White)
                    }
                }
            }
        }
    }
}
