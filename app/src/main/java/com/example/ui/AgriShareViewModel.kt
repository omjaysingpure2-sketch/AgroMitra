package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ActivityItem
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.data.model.ChatMessage
import com.example.data.model.Equipment
import com.example.data.model.NotificationItem
import com.example.data.model.PlantScan
import com.example.data.model.SoilReport
import com.example.data.model.UserProfile
import com.example.data.repository.AgriShareRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class ScreenDestination {
    HOME,
    DASHBOARD,
    SCANNER,
    EQUIPMENT_BOOKING,
    AI_CHAT,
    PROFILE,
    SETTINGS,
    PLANT_HEALTH,
    SOIL_HEALTH,
    FERTILIZER_GUIDE,
    WATER_MANAGEMENT,
    MY_REPORTS
}

class AgriShareViewModel(
    private val repository: AgriShareRepository
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(ScreenDestination.HOME)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLocation = MutableStateFlow("Pune, Maharashtra")
    val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

    private val _selectedEquipmentForDetail = MutableStateFlow<Equipment?>(null)
    val selectedEquipmentForDetail: StateFlow<Equipment?> = _selectedEquipmentForDetail.asStateFlow()

    private val _bookingDialogOpen = MutableStateFlow(false)
    val bookingDialogOpen: StateFlow<Boolean> = _bookingDialogOpen.asStateFlow()

    private val _equipmentToBook = MutableStateFlow<Equipment?>(null)
    val equipmentToBook: StateFlow<Equipment?> = _equipmentToBook.asStateFlow()

    private val _bookingSuccessMessage = MutableStateFlow<String?>(null)
    val bookingSuccessMessage: StateFlow<String?> = _bookingSuccessMessage.asStateFlow()

    // Owner role / farmer role toggle for Dashboard
    private val _activeDashboardRole = MutableStateFlow("Farmer")
    val activeDashboardRole: StateFlow<String> = _activeDashboardRole.asStateFlow()

    // Scanner state
    private val _scannedPlantResult = MutableStateFlow<PlantScan?>(null)
    val scannedPlantResult: StateFlow<PlantScan?> = _scannedPlantResult.asStateFlow()

    private val _isAnalyzingPlant = MutableStateFlow(false)
    val isAnalyzingPlant: StateFlow<Boolean> = _isAnalyzingPlant.asStateFlow()

    // AI Chat state
    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Data streams from repository
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allEquipment: StateFlow<List<Equipment>> = repository.allEquipment
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPlantScans: StateFlow<List<PlantScan>> = repository.allPlantScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSoilReports: StateFlow<List<SoilReport>> = repository.allSoilReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentActivities: StateFlow<List<ActivityItem>> = repository.recentActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onLocationChange(location: String) {
        _selectedLocation.value = location
    }

    fun setDashboardRole(role: String) {
        _activeDashboardRole.value = role
    }

    fun showEquipmentDetail(equipment: Equipment) {
        _selectedEquipmentForDetail.value = equipment
    }

    fun dismissEquipmentDetail() {
        _selectedEquipmentForDetail.value = null
    }

    fun openBookingDialog(equipment: Equipment) {
        _equipmentToBook.value = equipment
        _bookingDialogOpen.value = true
    }

    fun dismissBookingDialog() {
        _bookingDialogOpen.value = false
        _equipmentToBook.value = null
    }

    fun submitBooking(
        equipment: Equipment,
        startDate: String,
        durationDays: Int,
        notes: String
    ) {
        viewModelScope.launch {
            val user = userProfile.value
            val farmerName = user?.fullName ?: "Farmer"
            val farmerPhone = user?.phone ?: "+91 98765 43210"
            val location = user?.location ?: selectedLocation.value

            repository.bookEquipment(
                equipment = equipment,
                startDate = startDate,
                durationDays = durationDays,
                farmerName = farmerName,
                farmerPhone = farmerPhone,
                location = location,
                notes = notes
            )
            _bookingSuccessMessage.value = "Booking request submitted successfully for ${equipment.title}!"
            dismissBookingDialog()
            delay(3500)
            _bookingSuccessMessage.value = null
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId)
        }
    }

    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, status)
        }
    }

    fun addEquipmentListing(
        title: String,
        category: String,
        hp: String,
        dailyRate: Int,
        location: String,
        description: String,
        imageDrawableName: String = "equip_tractor"
    ) {
        viewModelScope.launch {
            val user = userProfile.value
            val newEq = Equipment(
                id = "eq_${System.currentTimeMillis()}",
                title = title,
                subtitle = "$category • $hp",
                dailyRate = dailyRate,
                location = location,
                distanceKm = 5,
                rating = 5.0,
                reviewCount = 1,
                isAvailable = true,
                imageDrawableName = imageDrawableName,
                ownerName = user?.fullName ?: "Equipment Owner",
                ownerPhone = user?.phone ?: "+91 98765 43210",
                category = category,
                hp = hp,
                description = description
            )
            repository.addEquipmentListing(newEq)
        }
    }

    fun toggleEquipmentAvailability(equipmentId: String, available: Boolean) {
        viewModelScope.launch {
            repository.toggleEquipmentAvailability(equipmentId, available)
        }
    }

    fun analyzePlant(cropGuess: String = "Tomato", symptomType: Int = 0) {
        viewModelScope.launch {
            _isAnalyzingPlant.value = true
            delay(1200) // Realistic processing delay
            val scan = when (symptomType % 3) {
                0 -> PlantScan(
                    id = "scan_${UUID.randomUUID()}",
                    cropName = if (cropGuess.isBlank()) "Tomato" else cropGuess,
                    diseaseName = "Early Blight (Alternaria solani)",
                    isHealthy = false,
                    confidenceScore = 93,
                    symptoms = "Concentric dark brown circular spots resembling a target board on lower leaves, surrounded by yellow halos.",
                    recommendedSteps = "Prune infected lower foliage immediately. Apply Mancozeb 75% WP @ 2.5g/liter or Copper Oxychloride. Ensure good air circulation.",
                    fertilizerGuidance = "Avoid excess nitrogen foliage flush; supplement Potassium and Calcium to strengthen plant cell walls.",
                    wateringGuidance = "Switch to drip irrigation. Do not use overhead sprinklers which spread fungal spores.",
                    dateFormatted = "Today, Just now"
                )
                1 -> PlantScan(
                    id = "scan_${UUID.randomUUID()}",
                    cropName = if (cropGuess.isBlank()) "Wheat" else cropGuess,
                    diseaseName = "Healthy (Optimal Vitality)",
                    isHealthy = true,
                    confidenceScore = 97,
                    symptoms = "Vigorous green leaves, strong tillering, absence of powdery mildew or rust pustules.",
                    recommendedSteps = "Continue current irrigation schedule. Monitor for aphids as heading stage approaches.",
                    fertilizerGuidance = "Apply top dressing of Urea (N) with irrigation at crown root initiation.",
                    wateringGuidance = "Maintain adequate soil moisture during tillering and flowering.",
                    dateFormatted = "Today, Just now"
                )
                else -> PlantScan(
                    id = "scan_${UUID.randomUUID()}",
                    cropName = if (cropGuess.isBlank()) "Cotton" else cropGuess,
                    diseaseName = "Bacterial Blight (Angular Leaf Spot)",
                    isHealthy = false,
                    confidenceScore = 89,
                    symptoms = "Water-soaked angular lesions bounded by leaf veins turning brown/black.",
                    recommendedSteps = "Spray Copper Oxychloride 50 WP (30g) + Streptocycline (1g) in 10 liters of water. Avoid field operations when foliage is wet.",
                    fertilizerGuidance = "Apply balanced NPK with special emphasis on Potash to induce systemic acquired resistance.",
                    wateringGuidance = "Avoid flood irrigation water stagnation around root zones.",
                    dateFormatted = "Today, Just now"
                )
            }
            _scannedPlantResult.value = scan
            _isAnalyzingPlant.value = false
        }
    }

    fun saveCurrentScan() {
        val scan = _scannedPlantResult.value ?: return
        viewModelScope.launch {
            repository.savePlantScan(scan)
            _bookingSuccessMessage.value = "Report saved to My Reports!"
            delay(2500)
            _bookingSuccessMessage.value = null
        }
    }

    fun deletePlantScan(id: String) {
        viewModelScope.launch {
            repository.deletePlantScan(id)
        }
    }

    fun saveSoilReport(
        cropName: String,
        soilType: String,
        ph: Double,
        nLevel: String,
        pLevel: String,
        kLevel: String,
        moisturePercent: Int
    ) {
        viewModelScope.launch {
            val summary = when {
                ph < 6.5 -> "Acidic soil ($ph pH). May limit phosphorus and magnesium availability."
                ph > 7.5 -> "Alkaline/Calcareous soil ($ph pH). Zinc and iron micronutrient lock-up likely."
                else -> "Neutral optimal soil ($ph pH), excellent microbial nutrient uptake capacity."
            }
            val recs = when {
                ph < 6.5 -> "Apply Agricultural Lime (Calcium Carbonate) @ 200 kg/acre. Use rock phosphate and well-decomposed FYM."
                ph > 7.5 -> "Apply Agricultural Gypsum @ 150 kg/acre with organic compost. Use Ammonium Sulphate instead of Urea."
                else -> "Maintain fertility with 5 tonnes farmyard manure per acre. Apply basal NPK per targeted crop yield."
            }

            val report = SoilReport(
                id = "soil_${System.currentTimeMillis()}",
                cropName = cropName.ifBlank { "General Field" },
                soilType = soilType,
                ph = ph,
                nitrogen = nLevel,
                phosphorus = pLevel,
                potassium = kLevel,
                moisturePercent = moisturePercent,
                summary = summary,
                recommendations = recs,
                dateFormatted = "Today"
            )
            repository.saveSoilReport(report)
            _bookingSuccessMessage.value = "Soil Health Report saved!"
            delay(2500)
            _bookingSuccessMessage.value = null
        }
    }

    fun deleteSoilReport(id: String) {
        viewModelScope.launch {
            repository.deleteSoilReport(id)
        }
    }

    fun sendUserChatMessage(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(prompt)
            _isAiThinking.value = true
            delay(1000)

            val pLower = prompt.lowercase()
            val answer = when {
                "fertilizer" in pLower || "urea" in pLower || "npk" in pLower ->
                    "🌿 **Fertilizer Recommendations**\n\nFor balanced vegetative growth, maintain N:P:K ratio appropriate for your crop stage:\n• **Basal dose:** 50% Nitrogen, 100% Phosphorus and 100% Potash at sowing.\n• **Top dressing:** Remaining Nitrogen split into two doses at 30 and 45 days after sowing.\n• *Eco Tip:* Incorporate Neem-coated Urea to prevent nitrogen leaching."
                "disease" in pLower || "blight" in pLower || "fungus" in pLower || "rust" in pLower || "spot" in pLower ->
                    "🔍 **Crop Health & Disease Control**\n\n1. Inspect the underside of leaves for mycelium or fungal spore clusters.\n2. Prune and destroy infected plant debris outside the field.\n3. Foliar preventative spray: Mancozeb 75% WP @ 2.5g/L or Trichoderma viride bio-fungicide.\n4. Avoid overhead sprinkler irrigation during humid weather."
                "water" in pLower || "irrigation" in pLower || "moisture" in pLower || "drip" in pLower ->
                    "💧 **Irrigation & Water Management**\n\n• For medium black soils in Maharashtra, adopt drip irrigation to save up to 45% water.\n• Critical watering stages:\n  - Crown Root Initiation\n  - Flowering / Booting Stage\n  - Grain/Fruit Filling Stage\n• Mulching with agricultural straw reduces evaporation losses by up to 30%."
                "equipment" in pLower || "tractor" in pLower || "rotavator" in pLower || "rent" in pLower ->
                    "🚜 **AgriShare Equipment Advice**\n\n• For primary tillage in hard soils, a 45+ HP tractor paired with a reversible mouldboard plough ensures 10-12 inch deep furrow.\n• For seedbed finishing, book a 7-foot Rotavator (₹800/day) to achieve fine tilth in single run.\n• You can check nearby verified equipment under the **Equipment Booking** tab."
                "soil" in pLower || "ph" in pLower ->
                    "🌱 **Soil Health Guide**\n\n• Ideal pH for most crops is 6.5 to 7.5.\n• If pH is high (>8.0), add green manuring (Dhaincha/Sunn hemp) and Gypsum.\n• Soil organic carbon should ideally exceed 0.75% for optimal moisture retention."
                else ->
                    "🌾 **AgriShare Assistant Advice**\n\nThank you for your query about \"$prompt\".\n• Ensure crop rotation with legumes (chickpea/soybean) to replenish natural soil nitrogen.\n• Keep checking soil moisture before applying foliar nutrients.\n• Feel free to scan a leaf using our **Plant Scanner** for instant AI diagnosis!"
            }
            repository.addAiResponse(answer)
            _isAiThinking.value = false
        }
    }

    fun updateUserProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
            _bookingSuccessMessage.value = "Profile updated successfully!"
            delay(2500)
            _bookingSuccessMessage.value = null
        }
    }
}
