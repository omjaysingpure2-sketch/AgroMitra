package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment")
data class Equipment(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String = "",
    val dailyRate: Int,
    val location: String,
    val distanceKm: Int,
    val rating: Double,
    val reviewCount: Int,
    val isAvailable: Boolean = true,
    val imageDrawableName: String,
    val ownerName: String = "Ramesh Gaikwad",
    val ownerPhone: String = "+91 94220 12345",
    val category: String = "Tractor", // Tractor, Tillage, Harvesting, Seeding, Irrigation
    val hp: String = "45 HP",
    val description: String = "Reliable heavy-duty agricultural machinery suitable for multiple soil types and field preparation."
)
