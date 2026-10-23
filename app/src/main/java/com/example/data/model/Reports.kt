package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_scans")
data class PlantScan(
    @PrimaryKey val id: String,
    val cropName: String,
    val diseaseName: String,
    val isHealthy: Boolean,
    val confidenceScore: Int, // e.g. 94%
    val symptoms: String,
    val recommendedSteps: String,
    val fertilizerGuidance: String,
    val wateringGuidance: String,
    val dateFormatted: String,
    val imagePath: String = ""
)

@Entity(tableName = "soil_reports")
data class SoilReport(
    @PrimaryKey val id: String,
    val cropName: String,
    val soilType: String,
    val ph: Double,
    val nitrogen: String, // "Medium (280 kg/ha)"
    val phosphorus: String, // "High (45 kg/ha)"
    val potassium: String, // "Adequate (310 kg/ha)"
    val moisturePercent: Int,
    val summary: String,
    val recommendations: String,
    val dateFormatted: String
)
