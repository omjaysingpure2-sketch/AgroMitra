package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class BookingStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,
    COMPLETED
}

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey val id: String,
    val equipmentId: String,
    val equipmentTitle: String,
    val dailyRate: Int,
    val startDate: String,
    val durationDays: Int,
    val totalCost: Int,
    val status: BookingStatus = BookingStatus.PENDING,
    val bookingTime: String,
    val farmerName: String,
    val farmerPhone: String,
    val location: String,
    val notes: String = ""
)
