package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "current_user",
    val fullName: String = "Om Jaysingpure",
    val role: String = "Student", // "Student", "Farmer", "Equipment Owner"
    val email: String = "omjaysingpure2@gmail.com",
    val phone: String = "+91 98765 43210",
    val location: String = "Pune, Maharashtra",
    val village: String = "Haveli",
    val district: String = "Pune",
    val state: String = "Maharashtra",
    val farmAreaAcres: String = "5.5",
    val age: String = "23",
    val isLoggedIn: Boolean = true
)
