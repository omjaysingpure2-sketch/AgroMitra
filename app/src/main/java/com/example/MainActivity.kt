package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.AgriShareRepository
import com.example.ui.AgriShareViewModel
import com.example.ui.ScreenDestination
import com.example.ui.components.BookingModal
import com.example.ui.components.NAV_ITEMS
import com.example.ui.components.Sidebar
import com.example.ui.components.TopHeader
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EquipmentBookingScreen
import com.example.ui.screens.FertilizerGuideScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyReportsScreen
import com.example.ui.screens.PlantHealthScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SoilHealthScreen
import com.example.ui.screens.WaterManagementScreen
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = AgriShareRepository(database.dao())

        setContent {
            MyApplicationTheme {
                val viewModel: AgriShareViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return AgriShareViewModel(repository) as T
                        }
                    }
                )
                AgriShareApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AgriShareApp(viewModel: AgriShareViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val allEquipment by viewModel.allEquipment.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val allPlantScans by viewModel.allPlantScans.collectAsState()
    val allSoilReports by viewModel.allSoilReports.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val activeRole by viewModel.activeDashboardRole.collectAsState()

    val selectedEquipmentForDetail by viewModel.selectedEquipmentForDetail.collectAsState()
    val bookingDialogOpen by viewModel.bookingDialogOpen.collectAsState()
    val equipmentToBook by viewModel.equipmentToBook.collectAsState()
    val bookingSuccessMessage by viewModel.bookingSuccessMessage.collectAsState()

    val scannedPlantResult by viewModel.scannedPlantResult.collectAsState()
    val isAnalyzingPlant by viewModel.isAnalyzingPlant.collectAsState()

    // Handle back button when navigating into sub-screens
    BackHandler(enabled = currentScreen != ScreenDestination.HOME) {
        viewModel.navigateTo(ScreenDestination.HOME)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val isWide = maxWidth >= 700.dp

        if (isWide) {
            // Desktop / Tablet layout matching reference screenshot with persistent left sidebar
            Row(modifier = Modifier.fillMaxSize()) {
                Sidebar(
                    currentScreen = currentScreen,
                    onNavigate = { dest ->
                        viewModel.navigateTo(dest)
                    }
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    TopHeader(
                        userProfile = userProfile,
                        selectedLocation = selectedLocation,
                        notifications = notifications,
                        onLocationChange = { viewModel.onLocationChange(it) },
                        onNavigateToProfile = { viewModel.navigateTo(ScreenDestination.PROFILE) },
                        onOpenDrawer = null
                    )

                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        ScreenRouter(
                            currentScreen = currentScreen,
                            viewModel = viewModel,
                            userProfile = userProfile,
                            allEquipment = allEquipment,
                            allBookings = allBookings,
                            allPlantScans = allPlantScans,
                            allSoilReports = allSoilReports,
                            recentActivities = recentActivities,
                            notifications = notifications,
                            chatMessages = chatMessages,
                            isAiThinking = isAiThinking,
                            searchQuery = searchQuery,
                            selectedLocation = selectedLocation,
                            activeRole = activeRole,
                            scannedPlantResult = scannedPlantResult,
                            isAnalyzingPlant = isAnalyzingPlant
                        )
                    }
                }
            }
        } else {
            // Compact mobile layout with drawer, responsive header, and bottom navigation bar
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = AgriDarkGreen,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Sidebar(
                            currentScreen = currentScreen,
                            onNavigate = { dest ->
                                viewModel.navigateTo(dest)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    bottomBar = {
                        AgriBottomNavigation(
                            currentScreen = currentScreen,
                            onNavigate = { dest -> viewModel.navigateTo(dest) }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TopHeader(
                            userProfile = userProfile,
                            selectedLocation = selectedLocation,
                            notifications = notifications,
                            onLocationChange = { viewModel.onLocationChange(it) },
                            onNavigateToProfile = { viewModel.navigateTo(ScreenDestination.PROFILE) },
                            onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                        )

                        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            ScreenRouter(
                                currentScreen = currentScreen,
                                viewModel = viewModel,
                                userProfile = userProfile,
                                allEquipment = allEquipment,
                                allBookings = allBookings,
                                allPlantScans = allPlantScans,
                                allSoilReports = allSoilReports,
                                recentActivities = recentActivities,
                                notifications = notifications,
                                chatMessages = chatMessages,
                                isAiThinking = isAiThinking,
                                searchQuery = searchQuery,
                                selectedLocation = selectedLocation,
                                activeRole = activeRole,
                                scannedPlantResult = scannedPlantResult,
                                isAnalyzingPlant = isAnalyzingPlant
                            )
                        }
                    }
                }
            }
        }

        // Equipment Detail / Booking Modal
        val eqForModal = equipmentToBook ?: selectedEquipmentForDetail
        if (eqForModal != null && (bookingDialogOpen || selectedEquipmentForDetail != null)) {
            BookingModal(
                equipment = eqForModal,
                onDismiss = {
                    viewModel.dismissBookingDialog()
                    viewModel.dismissEquipmentDetail()
                },
                onSubmitBooking = { eq, startDate, days, notes ->
                    viewModel.submitBooking(eq, startDate, days, notes)
                    viewModel.dismissEquipmentDetail()
                }
            )
        }

        // Success Toast/Snackbar
        AnimatedVisibility(
            visible = bookingSuccessMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AgriDarkGreen)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = bookingSuccessMessage ?: "",
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun AgriBottomNavigation(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        NAV_ITEMS.take(6).forEach { item ->
            val isSelected = currentScreen == item.destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = if (item.destination == ScreenDestination.EQUIPMENT_BOOKING) "Booking" else item.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AgriDarkGreen,
                    selectedTextColor = AgriDarkGreen,
                    indicatorColor = Color(0xFFDCFCE7),
                    unselectedIconColor = Color(0xFF6B7280),
                    unselectedTextColor = Color(0xFF6B7280)
                )
            )
        }
    }
}

@Composable
fun ScreenRouter(
    currentScreen: ScreenDestination,
    viewModel: AgriShareViewModel,
    userProfile: com.example.data.model.UserProfile?,
    allEquipment: List<com.example.data.model.Equipment>,
    allBookings: List<com.example.data.model.Booking>,
    allPlantScans: List<com.example.data.model.PlantScan>,
    allSoilReports: List<com.example.data.model.SoilReport>,
    recentActivities: List<com.example.data.model.ActivityItem>,
    notifications: List<com.example.data.model.NotificationItem>,
    chatMessages: List<com.example.data.model.ChatMessage>,
    isAiThinking: Boolean,
    searchQuery: String,
    selectedLocation: String,
    activeRole: String,
    scannedPlantResult: com.example.data.model.PlantScan?,
    isAnalyzingPlant: Boolean
) {
    when (currentScreen) {
        ScreenDestination.HOME -> HomeScreen(
            userProfile = userProfile,
            equipmentList = allEquipment,
            recentActivities = recentActivities,
            notifications = notifications,
            searchQuery = searchQuery,
            selectedLocation = selectedLocation,
            onSearchChange = { viewModel.onSearchQueryChange(it) },
            onLocationChange = { viewModel.onLocationChange(it) },
            onNavigate = { viewModel.navigateTo(it) },
            onSelectEquipment = { viewModel.showEquipmentDetail(it) }
        )
        ScreenDestination.DASHBOARD -> DashboardScreen(
            userProfile = userProfile,
            allEquipment = allEquipment,
            allBookings = allBookings,
            allPlantScans = allPlantScans,
            allSoilReports = allSoilReports,
            activeRole = activeRole,
            onRoleChange = { viewModel.setDashboardRole(it) },
            onNavigate = { viewModel.navigateTo(it) }
        )
        ScreenDestination.SCANNER -> ScannerScreen(
            scannedResult = scannedPlantResult,
            isAnalyzing = isAnalyzingPlant,
            onAnalyze = { guess, sym -> viewModel.analyzePlant(guess, sym) },
            onSaveReport = { viewModel.saveCurrentScan() }
        )
        ScreenDestination.EQUIPMENT_BOOKING -> EquipmentBookingScreen(
            equipmentList = allEquipment,
            myBookings = allBookings,
            onSelectEquipment = { viewModel.showEquipmentDetail(it) },
            onCancelBooking = { viewModel.cancelBooking(it) },
            onUpdateBookingStatus = { id, status -> viewModel.updateBookingStatus(id, status) },
            onAddListing = { title, cat, hp, rate, loc, desc ->
                viewModel.addEquipmentListing(title, cat, hp, rate, loc, desc)
            },
            onToggleAvailability = { id, avail -> viewModel.toggleEquipmentAvailability(id, avail) }
        )
        ScreenDestination.AI_CHAT -> AiChatScreen(
            messages = chatMessages,
            isThinking = isAiThinking,
            onSendMessage = { viewModel.sendUserChatMessage(it) }
        )
        ScreenDestination.PROFILE -> ProfileScreen(
            userProfile = userProfile,
            onUpdateProfile = { viewModel.updateUserProfile(it) },
            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
        )
        ScreenDestination.SETTINGS -> SettingsScreen(
            selectedLocation = selectedLocation,
            onLocationChange = { viewModel.onLocationChange(it) },
            onSignOutToggle = {
                val current = userProfile
                if (current != null) {
                    viewModel.updateUserProfile(current.copy(isLoggedIn = !current.isLoggedIn))
                }
            },
            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
        )
        ScreenDestination.PLANT_HEALTH -> PlantHealthScreen(
            savedScans = allPlantScans,
            onOpenScanner = { viewModel.navigateTo(ScreenDestination.SCANNER) },
            onOpenReports = { viewModel.navigateTo(ScreenDestination.MY_REPORTS) },
            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
        )
        ScreenDestination.SOIL_HEALTH -> SoilHealthScreen(
            onSaveReport = { crop, type, ph, n, p, k, moist ->
                viewModel.saveSoilReport(crop, type, ph, n, p, k, moist)
            },
            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
        )
        ScreenDestination.FERTILIZER_GUIDE -> FertilizerGuideScreen(
            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
        )
        ScreenDestination.WATER_MANAGEMENT -> WaterManagementScreen(
            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
        )
        ScreenDestination.MY_REPORTS -> MyReportsScreen(
            scans = allPlantScans,
            soilReports = allSoilReports,
            onDeleteScan = { viewModel.deletePlantScan(it) },
            onDeleteSoilReport = { viewModel.deleteSoilReport(it) },
            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
        )
    }
}
