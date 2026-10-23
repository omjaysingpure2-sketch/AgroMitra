package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary

@Composable
fun FertilizerGuideScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCrop by remember { mutableStateOf("Wheat") }
    var selectedStage by remember { mutableStateOf("Vegetative (Crown Root)") }
    var selectedSeason by remember { mutableStateOf("Rabi (Winter)") }

    val crops = listOf("Wheat", "Tomato", "Sugarcane", "Soybean", "Cotton")
    val stages = listOf("Sowing / Basal", "Vegetative (Crown Root)", "Flowering / Booting", "Grain / Fruit Development")
    val seasons = listOf("Kharif (Monsoon)", "Rabi (Winter)", "Zaid (Summer)")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AgriDarkGreen)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("Scientific Fertilizer Guide", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                Text("Crop stage dosage, application methods, and soil environmental safety", fontSize = 11.sp, color = AgriTextSecondary)
            }
        }

        // Selection Cards
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Crop
                Text("Select Crop", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    crops.forEach { c ->
                        val isSelected = selectedCrop == c
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AgriDarkGreen else Color(0xFFF3F4F6))
                                .clickable { selectedCrop = c }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(c, fontSize = 11.sp, color = if (isSelected) Color.White else AgriTextPrimary)
                        }
                    }
                }

                // Stage
                Text("Growth Stage", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                stages.forEach { s ->
                    val isSelected = selectedStage == s
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFE8F5E9) else Color.Transparent)
                            .clickable { selectedStage = s }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = s,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) AgriGreenPrimary else AgriTextPrimary
                        )
                    }
                }

                // Season
                Text("Season", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    seasons.forEach { sn ->
                        val isSelected = selectedSeason == sn
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AgriGreenPrimary else Color(0xFFF3F4F6))
                                .clickable { selectedSeason = sn }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(sn, fontSize = 11.sp, color = if (isSelected) Color.White else AgriTextPrimary)
                        }
                    }
                }
            }
        }

        // Recommendations Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp))
                .testTag("fertilizer_recommendation_card"),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Recommended Fertilization for $selectedCrop ($selectedStage)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = AgriDarkGreen
                )

                HorizontalDivider(color = AgriBorder)

                when (selectedCrop) {
                    "Wheat" -> {
                        FertilizerItem(
                            fertilizer = "Neem Coated Urea (46% N) + MOP (60% K₂O)",
                            dosage = "Urea @ 30 kg/acre + MOP @ 15 kg/acre",
                            reason = "Promotes active crown root formation and tillering without causing excessive vegetative elongation.",
                            timing = "Apply right before the first irrigation (21 days after sowing).",
                            method = "Broadcast evenly in moist soil; do not apply on dry cracked soil."
                        )
                    }
                    "Tomato" -> {
                        FertilizerItem(
                            fertilizer = "Water Soluble NPK 19:19:19 + Micronutrient chelate",
                            dosage = "2.5 kg/acre per fertigation round (split weekly)",
                            reason = "Maintains balanced vegetative canopy and root vitality without salt stress.",
                            timing = "During active vegetative branching prior to cluster flowering.",
                            method = "Drip fertigation via Venturi injector in the morning hours."
                        )
                    }
                    "Sugarcane" -> {
                        FertilizerItem(
                            fertilizer = "DAP (18:46:0) + Urea + Ammonium Sulphate",
                            dosage = "DAP @ 50 kg/acre + Urea @ 25 kg/acre",
                            reason = "Provides high phosphorus for deep cane root architecture and tillering.",
                            timing = "Basal application at planting or 45 days formative stage.",
                            method = "Placement in furrows covered by 2-3 inches of soil."
                        )
                    }
                    else -> {
                        FertilizerItem(
                            fertilizer = "Standard Balanced Complex 10:26:26 + FYM",
                            dosage = "50 kg/acre with 2 tonnes compost",
                            reason = "Steady release of macro and secondary nutrients.",
                            timing = "Basal application during field preparation.",
                            method = "Drill application 5cm beside seed furrow."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Safety and Environmental precautions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF0FDF4))
                        .padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Safety & Environmental Guardrails", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AgriDarkGreen)
                        Text(
                            text = "• Avoid applying urea prior to heavy forecasted rain to prevent groundwater nitrate runoff.\n• Wear gloves and protective glasses when dissolving fertilizer salts.\n• For micro-nutrient deficiencies, confirm with a local agricultural university laboratory.",
                            fontSize = 10.sp,
                            color = AgriTextSecondary,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FertilizerItem(
    fertilizer: String,
    dosage: String,
    reason: String,
    timing: String,
    method: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Fertilizer Formulation: $fertilizer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AgriTextPrimary)
        Text("Recommended Dosage: $dosage", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriGreenPrimary)
        Text("Rationale: $reason", fontSize = 11.sp, color = AgriTextSecondary)
        Text("Timing & Method: $timing ($method)", fontSize = 11.sp, color = AgriTextSecondary)
    }
}
