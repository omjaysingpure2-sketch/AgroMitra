package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityItem
import com.example.data.model.Booking
import com.example.data.model.ChatMessage
import com.example.data.model.Equipment
import com.example.data.model.NotificationItem
import com.example.data.model.PlantScan
import com.example.data.model.SoilReport
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface AgriShareDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 'current_user'")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 'current_user'")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(profile: UserProfile)

    // Equipment
    @Query("SELECT * FROM equipment")
    fun getAllEquipmentFlow(): Flow<List<Equipment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEquipment(list: List<Equipment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(item: Equipment)

    @Update
    suspend fun updateEquipment(item: Equipment)

    // Bookings
    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAllBookingsFlow(): Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Update
    suspend fun updateBooking(booking: Booking)

    @Query("DELETE FROM bookings WHERE id = :bookingId")
    suspend fun deleteBooking(bookingId: String)

    // Plant Scans
    @Query("SELECT * FROM plant_scans ORDER BY id DESC")
    fun getAllPlantScansFlow(): Flow<List<PlantScan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlantScan(scan: PlantScan)

    @Query("DELETE FROM plant_scans WHERE id = :id")
    suspend fun deletePlantScan(id: String)

    // Soil Reports
    @Query("SELECT * FROM soil_reports ORDER BY id DESC")
    fun getAllSoilReportsFlow(): Flow<List<SoilReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoilReport(report: SoilReport)

    @Query("DELETE FROM soil_reports WHERE id = :id")
    suspend fun deleteSoilReport(id: String)

    // Recent Activities
    @Query("SELECT * FROM recent_activities")
    fun getAllActivitiesFlow(): Flow<List<ActivityItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(items: List<ActivityItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(item: ActivityItem)

    // Notifications
    @Query("SELECT * FROM notifications")
    fun getAllNotificationsFlow(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(items: List<NotificationItem>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationRead(id: String)

    // Chat
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessagesFlow(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)
}
