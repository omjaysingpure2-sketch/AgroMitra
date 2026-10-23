package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ActivityItem
import com.example.data.model.Equipment
import com.example.data.model.NotificationItem
import com.example.data.model.UserProfile
import com.example.ui.ScreenDestination
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenAccent
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriSurface
import com.example.ui.theme.AgriTextMuted
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary
import com.example.ui.theme.BluePastelBg
import com.example.ui.theme.BluePastelIcon
import com.example.ui.theme.CyanPastelBg
import com.example.ui.theme.CyanPastelIcon
import com.example.ui.theme.EmeraldPastelBg
import com.example.ui.theme.EmeraldPastelIcon
import com.example.ui.theme.GreenPastelBg
import com.example.ui.theme.GreenPastelIcon
import com.example.ui.theme.LavenderPastelBg
import com.example.ui.theme.LavenderPastelIcon
import com.example.ui.theme.MintPastelBg
import com.example.ui.theme.MintPastelIcon
import com.example.ui.theme.PinkPastelBg
import com.example.ui.theme.PinkPastelIcon
import com.example.ui.theme.PurplePastelBg
import com.example.ui.theme.PurplePastelIcon
import com.example.ui.theme.RatingGold
import com.example.ui.theme.TanPastelBg
import com.example.ui.theme.TanPastelIcon
import com.example.ui.theme.TealPastelBg
import com.example.ui.theme.TealPastelIcon

data class QuickAccessItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val bgColor: Color,
    val iconColor: Color,
    val destination: ScreenDestination,
    val testTag: String
)

@Composable
fun HomeScreen(
    userProfile: UserProfile?,
    equipmentList: List<Equipment>,
    recentActivities: List<ActivityItem>,
    notifications: List<NotificationItem>,
    searchQuery: String,
    selectedLocation: String,
    onSearchChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    onSelectEquipment: (Equipment) -> Unit,
    modifier: Modifier = Modifier
) {
    val quickAccessItems = remember {
        listOf(
            QuickAccessItem("Dashboard", "View your farming overview", Icons.Default.Assessment, MintPastelBg, MintPastelIcon, ScreenDestination.DASHBOARD, "qa_dashboard"),
            QuickAccessItem("Plant Scanner", "Scan and analyze plants", Icons.Default.CropFree, CyanPastelBg, CyanPastelIcon, ScreenDestination.SCANNER, "qa_scanner"),
            QuickAccessItem("Plant Health", "Check plant health reports", Icons.Default.LocalFlorist, GreenPastelBg, GreenPastelIcon, ScreenDestination.PLANT_HEALTH, "qa_plant_health"),
            QuickAccessItem("Soil Health", "Analyze soil condition", Icons.Default.Terrain, TanPastelBg, TanPastelIcon, ScreenDestination.SOIL_HEALTH, "qa_soil_health"),
            QuickAccessItem("Fertilizer Guide", "Get fertilizer suggestions", Icons.Default.Spa, TealPastelBg, TealPastelIcon, ScreenDestination.FERTILIZER_GUIDE, "qa_fertilizer_guide"),
            QuickAccessItem("Water Management", "Plan irrigation needs", Icons.Default.WaterDrop, BluePastelBg, BluePastelIcon, ScreenDestination.WATER_MANAGEMENT, "qa_water_mgmt"),
            QuickAccessItem("Find Equipment", "Browse & book machinery", Icons.Default.Agriculture, PurplePastelBg, PurplePastelIcon, ScreenDestination.EQUIPMENT_BOOKING, "qa_find_equipment"),
            QuickAccessItem("My Bookings", "Track your bookings", Icons.Default.DateRange, PinkPastelBg, PinkPastelIcon, ScreenDestination.EQUIPMENT_BOOKING, "qa_my_bookings"),
            QuickAccessItem("AI Farming Chat", "Ask farming questions", Icons.Default.SmartToy, EmeraldPastelBg, EmeraldPastelIcon, ScreenDestination.AI_CHAT, "qa_ai_chat"),
            QuickAccessItem("My Reports", "View past scans & reports", Icons.Default.Description, LavenderPastelBg, LavenderPastelIcon, ScreenDestination.MY_REPORTS, "qa_my_reports")
        )
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize().background(AgriBackground)) {
        val isWide = maxWidth >= 840.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            if (isWide) {
                // Wide layout: 2-column layout (Main Feed 75% + Right Activity/Notifications 25%)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Left Main Feed
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(22.dp)
                    ) {
                        HeroBanner(
                            userProfile = userProfile,
                            searchQuery = searchQuery,
                            selectedLocation = selectedLocation,
                            onSearchChange = onSearchChange,
                            onLocationChange = onLocationChange
                        )

                        QuickAccessSection(
                            items = quickAccessItems,
                            onNavigate = onNavigate
                        )

                        FeaturedEquipmentSection(
                            equipmentList = equipmentList,
                            searchQuery = searchQuery,
                            onSelectEquipment = onSelectEquipment,
                            onViewAll = { onNavigate(ScreenDestination.EQUIPMENT_BOOKING) }
                        )

                        AiHelpCtaBanner(onChatNow = { onNavigate(ScreenDestination.AI_CHAT) })
                    }

                    // Right Side Column (Recent Activity + Notifications)
                    Column(
                        modifier = Modifier.width(280.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        RecentActivityCard(
                            activities = recentActivities,
                            onViewAll = { onNavigate(ScreenDestination.MY_REPORTS) }
                        )

                        NotificationsCard(
                            notifications = notifications,
                            onViewAll = { onNavigate(ScreenDestination.SETTINGS) }
                        )
                    }
                }
            } else {
                // Compact / Single Column layout
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    HeroBanner(
                        userProfile = userProfile,
                        searchQuery = searchQuery,
                        selectedLocation = selectedLocation,
                        onSearchChange = onSearchChange,
                        onLocationChange = onLocationChange
                    )

                    QuickAccessSection(
                        items = quickAccessItems,
                        onNavigate = onNavigate
                    )

                    FeaturedEquipmentSection(
                        equipmentList = equipmentList,
                        searchQuery = searchQuery,
                        onSelectEquipment = onSelectEquipment,
                        onViewAll = { onNavigate(ScreenDestination.EQUIPMENT_BOOKING) }
                    )

                    AiHelpCtaBanner(onChatNow = { onNavigate(ScreenDestination.AI_CHAT) })

                    RecentActivityCard(
                        activities = recentActivities,
                        onViewAll = { onNavigate(ScreenDestination.MY_REPORTS) }
                    )

                    NotificationsCard(
                        notifications = notifications,
                        onViewAll = { onNavigate(ScreenDestination.SETTINGS) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HeroBanner(
    userProfile: UserProfile?,
    searchQuery: String,
    selectedLocation: String,
    onSearchChange: (String) -> Unit,
    onLocationChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .testTag("hero_banner_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AgriBorder, RoundedCornerShape(20.dp))
        ) {
            val isWideHero = maxWidth >= 640.dp

            // Background Image on the right side
            Image(
                painter = painterResource(id = R.drawable.hero_farm_tractor),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isWideHero) 220.dp else 250.dp)
            )

            // Soft gradient overlay to keep text crystal clear on left
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isWideHero) 220.dp else 250.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = if (isWideHero) {
                                listOf(
                                    Color.White.copy(alpha = 0.96f),
                                    Color.White.copy(alpha = 0.90f),
                                    Color.White.copy(alpha = 0.70f),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    Color.White.copy(alpha = 0.96f),
                                    Color.White.copy(alpha = 0.90f),
                                    Color.White.copy(alpha = 0.75f)
                                )
                            }
                        )
                    )
            )

            // Slogan in cursive on top-right (on wide screens)
            if (isWideHero) {
                Text(
                    text = "Better Equipment.\nSmarter Farming.",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 24.dp, top = 20.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth(if (isWideHero) 0.72f else 1f)
            ) {
                val greetingName = if (userProfile?.isLoggedIn == true && userProfile.fullName.isNotBlank()) {
                    "Hello, ${userProfile.fullName} 👋"
                } else {
                    "Welcome to AgriShare 👋"
                }

                Text(
                    text = greetingName,
                    color = Color(0xFF1E3A2B),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Welcome to AgriShare",
                    color = Color(0xFF0F2E20),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Your Smart Farming Assistant.",
                    color = Color(0xFF4B5563),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar with Location Tag
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(24.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = AgriTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                "Search for equipment, crops, or farming advice...",
                                fontSize = 12.sp,
                                color = AgriTextMuted
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("home_search_input")
                    )

                    // Location Pill inside search
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF3F4F6))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = selectedLocation,
                            fontSize = 11.sp,
                            color = AgriTextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = AgriTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3 Benefit Badges
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BenefitBadge(Icons.Default.Spa, "Affordable Equipment Access")
                    BenefitBadge(Icons.Default.DateRange, "Easy Online Booking")
                    BenefitBadge(Icons.Default.People, "Connected Farming Community")
                }
            }
        }
    }
}

@Composable
fun BenefitBadge(icon: ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AgriGreenPrimary,
                modifier = Modifier.size(12.dp)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF1E3A2B),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun QuickAccessSection(
    items: List<QuickAccessItem>,
    onNavigate: (ScreenDestination) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quick Access",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AgriTextPrimary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onNavigate(ScreenDestination.DASHBOARD) }
                    .padding(4.dp)
            ) {
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    color = AgriGreenPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2 Rows of 5 items scrollable horizontally
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1 (first 5 cards)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items.take(5).forEach { item ->
                    QuickAccessCard(item = item, onClick = { onNavigate(item.destination) })
                }
            }
            // Row 2 (next 5 cards)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items.drop(5).take(5).forEach { item ->
                    QuickAccessCard(item = item, onClick = { onNavigate(item.destination) })
                }
            }
        }
    }
}

@Composable
fun QuickAccessCard(
    item: QuickAccessItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(item.testTag),
        colors = CardDefaults.cardColors(containerColor = AgriSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(AgriBorder, AgriBorder))),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(item.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgriTextPrimary,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.subtitle,
                fontSize = 10.sp,
                color = AgriTextSecondary,
                lineHeight = 12.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun FeaturedEquipmentSection(
    equipmentList: List<Equipment>,
    searchQuery: String,
    onSelectEquipment: (Equipment) -> Unit,
    onViewAll: () -> Unit
) {
    val filtered = if (searchQuery.isBlank()) {
        equipmentList
    } else {
        equipmentList.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true) ||
                    it.location.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Featured Equipment",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(onClick = onViewAll)
                    .padding(4.dp)
            ) {
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    color = AgriGreenPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal scroll list of equipment cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            filtered.forEach { eq ->
                EquipmentCard(equipment = eq, onClick = { onSelectEquipment(eq) })
            }

            // Carousel Right Arrow Pill
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, AgriBorder, CircleShape)
                    .clickable(onClick = onViewAll),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next equipment",
                    tint = AgriTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EquipmentCard(
    equipment: Equipment,
    onClick: () -> Unit
) {
    val imageRes = when (equipment.imageDrawableName) {
        "equip_rotavator" -> R.drawable.equip_rotavator
        "equip_harvester" -> R.drawable.equip_harvester
        "equip_seed_drill" -> R.drawable.equip_seed_drill
        else -> R.drawable.equip_tractor
    }

    Card(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, AgriBorder, RoundedCornerShape(14.dp))
            .testTag("equipment_card_${equipment.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = equipment.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Available Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AgriGreenAccent)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (equipment.isAvailable) "Available" else "Rented",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = equipment.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "₹${equipment.dailyRate} / day",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AgriDarkGreen
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AgriTextSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${equipment.location} • ${equipment.distanceKm} km",
                        fontSize = 10.sp,
                        color = AgriTextSecondary,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RatingGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${equipment.rating} (${equipment.reviewCount} reviews)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = AgriTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .testTag("view_details_${equipment.id}")
                ) {
                    Text(
                        text = "View Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AiHelpCtaBanner(onChatNow: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("ai_crops_cta_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AgriDarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F3B2A),
                            Color(0xFF1B553D),
                            Color(0xFF266649)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D56)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Need help with your crops?",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ask our AI assistant for expert advice, fertilizer suggestions and more.",
                        color = Color(0xFFD1FAE5),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onChatNow,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(38.dp)
                    .testTag("chat_now_button")
            ) {
                Text(
                    text = "Chat Now →",
                    color = AgriDarkGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RecentActivityCard(
    activities: List<ActivityItem>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AgriBorder, RoundedCornerShape(16.dp))
            .testTag("recent_activity_section"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onViewAll)
                ) {
                    Text(
                        text = "View All",
                        fontSize = 11.sp,
                        color = AgriGreenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            activities.forEach { act ->
                val (bgColor, iconColor, icon) = when (act.type) {
                    "plant_scan" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), Icons.Default.Spa)
                    "equipment_booked" -> Triple(Color(0xFFEBF5FF), Color(0xFF2563EB), Icons.Default.Agriculture)
                    "fertilizer" -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), Icons.Default.LocalFlorist)
                    else -> Triple(Color(0xFFE0F2F1), Color(0xFF00897B), Icons.Default.Forum)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = act.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AgriTextPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = act.subtitle,
                                fontSize = 11.sp,
                                color = AgriTextSecondary,
                                maxLines = 1
                            )
                        }
                    }

                    Text(
                        text = act.timeAgo,
                        fontSize = 10.sp,
                        color = AgriTextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationsCard(
    notifications: List<NotificationItem>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AgriBorder, RoundedCornerShape(16.dp))
            .testTag("notifications_section"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onViewAll)
                ) {
                    Text(
                        text = "View All",
                        fontSize = 11.sp,
                        color = AgriGreenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            notifications.forEach { notif ->
                val (bgColor, iconColor, icon) = when (notif.type) {
                    "booking_approved" -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), Icons.Default.Check)
                    "soil_ready" -> Triple(Color(0xFFFDF4E7), Color(0xFFA06126), Icons.Default.Terrain)
                    else -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), Icons.Default.Build)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = notif.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AgriTextPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = notif.subtitle,
                                fontSize = 11.sp,
                                color = AgriTextSecondary,
                                maxLines = 1
                            )
                        }
                    }

                    Text(
                        text = notif.timeAgo,
                        fontSize = 10.sp,
                        color = AgriTextMuted
                    )
                }
            }
        }
    }
}
