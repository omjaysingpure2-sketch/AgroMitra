package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CropFree
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.ScreenDestination
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriNavSelected

data class NavItemConfig(
    val destination: ScreenDestination,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

val NAV_ITEMS = listOf(
    NavItemConfig(ScreenDestination.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
    NavItemConfig(ScreenDestination.DASHBOARD, "Dashboard", Icons.Filled.GridView, Icons.Outlined.GridView, "nav_dashboard"),
    NavItemConfig(ScreenDestination.SCANNER, "Scanner", Icons.Filled.CropFree, Icons.Outlined.CropFree, "nav_scanner"),
    NavItemConfig(ScreenDestination.EQUIPMENT_BOOKING, "Equipment Booking", Icons.Filled.DateRange, Icons.Outlined.DateRange, "nav_equipment"),
    NavItemConfig(ScreenDestination.AI_CHAT, "AI Chat", Icons.Filled.Forum, Icons.Outlined.Forum, "nav_ai_chat"),
    NavItemConfig(ScreenDestination.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile"),
    NavItemConfig(ScreenDestination.SETTINGS, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings")
)

@Composable
fun Sidebar(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(220.dp)
            .background(AgriDarkGreen)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 18.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // AgriShare Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable { onNavigate(ScreenDestination.HOME) }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_agrishare_logo),
                    contentDescription = "AgriShare Logo",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row {
                        Text(
                            text = "Agri",
                            color = Color(0xFF4ADE80),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Share",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Share Equipment, Grow Together",
                        color = Color(0xFF9CA3AF),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Items
            NAV_ITEMS.forEach { item ->
                val isSelected = currentScreen == item.destination ||
                        (item.destination == ScreenDestination.EQUIPMENT_BOOKING &&
                                (currentScreen == ScreenDestination.EQUIPMENT_BOOKING))
                val bgColor = if (isSelected) AgriNavSelected else Color.Transparent
                val contentColor = if (isSelected) Color.White else Color(0xFFD1D5DB)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor)
                        .clickable { onNavigate(item.destination) }
                        .padding(horizontal = 14.dp, vertical = 11.dp)
                        .testTag(item.testTag)
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = item.title,
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                    )
                }
            }
        }

        // Bottom Decorative Footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_sidebar_tractor),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Smart Farming",
                color = Color(0xFFE5E7EB),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Better Tomorrow",
                color = Color(0xFF9CA3AF),
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
