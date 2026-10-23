package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_activities")
data class ActivityItem(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val timeAgo: String,
    val type: String // "plant_scan", "equipment_booked", "fertilizer", "ai_assistant"
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val timeAgo: String,
    val type: String, // "booking_approved", "soil_ready", "maintenance"
    val isRead: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
