package com.example.data.repository

import com.example.data.local.AgriShareDao
import com.example.data.model.ActivityItem
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.data.model.ChatMessage
import com.example.data.model.Equipment
import com.example.data.model.NotificationItem
import com.example.data.model.PlantScan
import com.example.data.model.SoilReport
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AgriShareRepository(private val dao: AgriShareDao) {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfEmpty()
        }
    }

    val userProfile: Flow<UserProfile?> = dao.getUserProfileFlow()
    val allEquipment: Flow<List<Equipment>> = dao.getAllEquipmentFlow()
    val allBookings: Flow<List<Booking>> = dao.getAllBookingsFlow()
    val allPlantScans: Flow<List<PlantScan>> = dao.getAllPlantScansFlow()
    val allSoilReports: Flow<List<SoilReport>> = dao.getAllSoilReportsFlow()
    val recentActivities: Flow<List<ActivityItem>> = dao.getAllActivitiesFlow()
    val notifications: Flow<List<NotificationItem>> = dao.getAllNotificationsFlow()
    val chatMessages: Flow<List<ChatMessage>> = dao.getAllChatMessagesFlow()

    private suspend fun seedInitialDataIfEmpty() {
        if (dao.getUserProfile() == null) {
            dao.insertOrUpdateUserProfile(
                UserProfile(
                    id = "current_user",
                    fullName = "Om Jaysingpure",
                    role = "Student",
                    email = "omjaysingpure2@gmail.com",
                    phone = "+91 98765 43210",
                    location = "Pune, Maharashtra",
                    village = "Haveli",
                    district = "Pune",
                    state = "Maharashtra",
                    farmAreaAcres = "5.5",
                    age = "23",
                    isLoggedIn = true
                )
            )
        }

        if (dao.getAllEquipmentFlow().firstOrNull()?.isEmpty() != false) {
            val initialEquipment = listOf(
                Equipment(
                    id = "eq_1",
                    title = "Tractor (45 HP)",
                    subtitle = "John Deere 5045D - Power Steering & Dual Clutch",
                    dailyRate = 1200,
                    location = "Pune, Maharashtra",
                    distanceKm = 12,
                    rating = 4.8,
                    reviewCount = 32,
                    isAvailable = true,
                    imageDrawableName = "equip_tractor",
                    ownerName = "Suresh Patil",
                    ownerPhone = "+91 98220 54321",
                    category = "Tractor",
                    hp = "45 HP",
                    description = "Well-maintained 45HP tractor equipped with hydraulic lift, ideal for rotavation, ploughing, and trailer transport."
                ),
                Equipment(
                    id = "eq_2",
                    title = "Rotavator",
                    subtitle = "7 Feet Heavy Duty Rotavator - Multi Speed",
                    dailyRate = 800,
                    location = "Pune, Maharashtra",
                    distanceKm = 15,
                    rating = 4.6,
                    reviewCount = 18,
                    isAvailable = true,
                    imageDrawableName = "equip_rotavator",
                    ownerName = "Mahesh Shinde",
                    ownerPhone = "+91 97654 32109",
                    category = "Tillage",
                    hp = "Min 40 HP required",
                    description = "Heavy duty 48 boron steel blades for excellent seedbed preparation in single pass. Compatible with 40-55 HP tractors."
                ),
                Equipment(
                    id = "eq_3",
                    title = "Harvester",
                    subtitle = "Multicrop Self-Propelled Combine Harvester",
                    dailyRate = 3500,
                    location = "Nashik, Maharashtra",
                    distanceKm = 48,
                    rating = 4.7,
                    reviewCount = 24,
                    isAvailable = true,
                    imageDrawableName = "equip_harvester",
                    ownerName = "Anand Deshmukh",
                    ownerPhone = "+91 94231 98765",
                    category = "Harvesting",
                    hp = "101 HP",
                    description = "High efficiency multicrop combine harvester with grain cleaner, suitable for wheat, soybean, paddy, and gram harvesting."
                ),
                Equipment(
                    id = "eq_4",
                    title = "Seed Drill",
                    subtitle = "Zero Tillage Multi-Crop Seed Cum Fertilizer Drill",
                    dailyRate = 900,
                    location = "Satara, Maharashtra",
                    distanceKm = 65,
                    rating = 4.5,
                    reviewCount = 16,
                    isAvailable = true,
                    imageDrawableName = "equip_seed_drill",
                    ownerName = "Dnyaneshwar Jadhav",
                    ownerPhone = "+91 99211 44556",
                    category = "Seeding",
                    hp = "35-50 HP",
                    description = "Calibrated dual hopper for simultaneous seed and basal fertilizer placement with accurate depth control."
                )
            )
            dao.insertAllEquipment(initialEquipment)
        }

        if (dao.getAllActivitiesFlow().firstOrNull()?.isEmpty() != false) {
            val initialActivities = listOf(
                ActivityItem("act_1", "Plant scan completed", "Tomato - Healthy", "2 hours ago", "plant_scan"),
                ActivityItem("act_2", "Equipment booked", "Tractor - 45 HP", "5 hours ago", "equipment_booked"),
                ActivityItem("act_3", "Fertilizer recommendation", "For Wheat crop", "1 day ago", "fertilizer"),
                ActivityItem("act_4", "New message from AI Assistant", "Crop care tips available", "1 day ago", "ai_assistant")
            )
            dao.insertActivities(initialActivities)
        }

        if (dao.getAllNotificationsFlow().firstOrNull()?.isEmpty() != false) {
            val initialNotifications = listOf(
                NotificationItem("notif_1", "Your booking is approved", "Rotavator - Apr 25, 2025", "2 hours ago", "booking_approved"),
                NotificationItem("notif_2", "Soil test report ready", "View your analysis results", "5 hours ago", "soil_ready"),
                NotificationItem("notif_3", "Maintenance reminder", "Tractor - Due in 7 days", "1 day ago", "maintenance")
            )
            dao.insertNotifications(initialNotifications)
        }

        if (dao.getAllBookingsFlow().firstOrNull()?.isEmpty() != false) {
            val initialBookings = listOf(
                Booking(
                    id = "book_101",
                    equipmentId = "eq_2",
                    equipmentTitle = "Rotavator",
                    dailyRate = 800,
                    startDate = "Apr 25, 2025",
                    durationDays = 2,
                    totalCost = 1600,
                    status = BookingStatus.APPROVED,
                    bookingTime = "2 hours ago",
                    farmerName = "Om Jaysingpure",
                    farmerPhone = "+91 98765 43210",
                    location = "Pune, Maharashtra"
                ),
                Booking(
                    id = "book_102",
                    equipmentId = "eq_1",
                    equipmentTitle = "Tractor (45 HP)",
                    dailyRate = 1200,
                    startDate = "Apr 18, 2025",
                    durationDays = 3,
                    totalCost = 3600,
                    status = BookingStatus.COMPLETED,
                    bookingTime = "5 days ago",
                    farmerName = "Om Jaysingpure",
                    farmerPhone = "+91 98765 43210",
                    location = "Pune, Maharashtra"
                )
            )
            initialBookings.forEach { dao.insertBooking(it) }
        }

        if (dao.getAllPlantScansFlow().firstOrNull()?.isEmpty() != false) {
            val initialScans = listOf(
                PlantScan(
                    id = "scan_1",
                    cropName = "Tomato",
                    diseaseName = "Healthy (No Disease Detected)",
                    isHealthy = true,
                    confidenceScore = 96,
                    symptoms = "Vibrant green foliage, sturdy stem, no fungal or bacterial lesions.",
                    recommendedSteps = "Continue current drip fertigation schedule. Monitor lower leaves weekly for early signs of blight.",
                    fertilizerGuidance = "Apply micronutrient spray (Zinc + Boron) during flowering stage.",
                    wateringGuidance = "Water 4-5 liters per plant every 2 days depending on temperature.",
                    dateFormatted = "Today, 10:30 AM"
                ),
                PlantScan(
                    id = "scan_2",
                    cropName = "Wheat",
                    diseaseName = "Yellow Rust (Puccinia striiformis)",
                    isHealthy = false,
                    confidenceScore = 92,
                    symptoms = "Yellow stripes of pustules running parallel along the leaf veins.",
                    recommendedSteps = "Apply foliar fungicide spray immediately during early morning. Avoid excessive nitrogen application.",
                    fertilizerGuidance = "Hold off top dressing nitrogen until fungal spread is controlled; supply Potash to strengthen cell walls.",
                    wateringGuidance = "Avoid overhead irrigation that wets foliage; use furrow or border irrigation.",
                    dateFormatted = "Yesterday, 3:15 PM"
                )
            )
            initialScans.forEach { dao.insertPlantScan(it) }
        }

        if (dao.getAllSoilReportsFlow().firstOrNull()?.isEmpty() != false) {
            val initialSoil = listOf(
                SoilReport(
                    id = "soil_1",
                    cropName = "Soybean & Wheat",
                    soilType = "Medium Black Soil",
                    ph = 7.4,
                    nitrogen = "Medium (265 kg/ha)",
                    phosphorus = "High (38 kg/ha)",
                    potassium = "High (320 kg/ha)",
                    moisturePercent = 42,
                    summary = "Slightly alkaline black soil with rich organic carbon and strong potassium reserves.",
                    recommendations = "Apply Gypsum @ 100 kg/acre to improve drainage. Use DAP in moderation; supplement Zinc Sulphate 10 kg/acre.",
                    dateFormatted = "Oct 20, 2026"
                )
            )
            initialSoil.forEach { dao.insertSoilReport(it) }
        }

        if (dao.getAllChatMessagesFlow().firstOrNull()?.isEmpty() != false) {
            dao.insertChatMessage(
                ChatMessage(
                    id = "msg_welcome",
                    text = "Hello Om! I am your AgriShare AI Farming Assistant. How can I help you today? You can ask me about crop diseases, organic fertilizer recipes, drip irrigation timings, or agricultural machinery guidance.",
                    isUser = false,
                    timestamp = System.currentTimeMillis() - 60000
                )
            )
        }
    }

    suspend fun updateProfile(profile: UserProfile) {
        dao.insertOrUpdateUserProfile(profile)
    }

    suspend fun bookEquipment(
        equipment: Equipment,
        startDate: String,
        durationDays: Int,
        farmerName: String,
        farmerPhone: String,
        location: String,
        notes: String
    ): Booking {
        val totalCost = equipment.dailyRate * durationDays
        val booking = Booking(
            id = "book_${System.currentTimeMillis()}",
            equipmentId = equipment.id,
            equipmentTitle = equipment.title,
            dailyRate = equipment.dailyRate,
            startDate = startDate,
            durationDays = durationDays,
            totalCost = totalCost,
            status = BookingStatus.PENDING,
            bookingTime = "Just now",
            farmerName = farmerName,
            farmerPhone = farmerPhone,
            location = location,
            notes = notes
        )
        dao.insertBooking(booking)

        // Add to activities
        dao.insertActivity(
            ActivityItem(
                id = "act_${UUID.randomUUID()}",
                title = "Equipment requested",
                subtitle = "${equipment.title} - $durationDays days",
                timeAgo = "Just now",
                type = "equipment_booked"
            )
        )
        return booking
    }

    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        val bookings = dao.getAllBookingsFlow().firstOrNull() ?: return
        val target = bookings.find { it.id == bookingId } ?: return
        val updated = target.copy(status = status)
        dao.updateBooking(updated)
    }

    suspend fun cancelBooking(bookingId: String) {
        updateBookingStatus(bookingId, BookingStatus.CANCELLED)
    }

    suspend fun addEquipmentListing(equipment: Equipment) {
        dao.insertEquipment(equipment)
    }

    suspend fun toggleEquipmentAvailability(equipmentId: String, available: Boolean) {
        val list = dao.getAllEquipmentFlow().firstOrNull() ?: return
        val item = list.find { it.id == equipmentId } ?: return
        dao.updateEquipment(item.copy(isAvailable = available))
    }

    suspend fun savePlantScan(scan: PlantScan) {
        dao.insertPlantScan(scan)
        dao.insertActivity(
            ActivityItem(
                id = "act_${UUID.randomUUID()}",
                title = "Plant scan completed",
                subtitle = "${scan.cropName} - ${if (scan.isHealthy) "Healthy" else scan.diseaseName}",
                timeAgo = "Just now",
                type = "plant_scan"
            )
        )
    }

    suspend fun deletePlantScan(id: String) {
        dao.deletePlantScan(id)
    }

    suspend fun saveSoilReport(report: SoilReport) {
        dao.insertSoilReport(report)
    }

    suspend fun deleteSoilReport(id: String) {
        dao.deleteSoilReport(id)
    }

    suspend fun sendChatMessage(userText: String): ChatMessage {
        val userMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            text = userText,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )
        dao.insertChatMessage(userMsg)
        return userMsg
    }

    suspend fun addAiResponse(responseText: String) {
        val aiMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            text = responseText,
            isUser = false,
            timestamp = System.currentTimeMillis()
        )
        dao.insertChatMessage(aiMsg)
    }
}
