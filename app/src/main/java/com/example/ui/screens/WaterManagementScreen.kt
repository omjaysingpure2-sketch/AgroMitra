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
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun WaterManagementScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCrop by remember { mutableStateOf("Sugarcane") }
    var selectedStage by remember { mutableStateOf("Tillering / Formative") }
    var selectedMethod by remember { mutableStateOf("Drip Irrigation") }
    var measuredMoisture by remember { mutableIntStateOf(48) }

    val crops = listOf("Sugarcane", "Wheat", "Tomato", "Cotton", "Soybean")
    val stages = listOf("Germination / Seedling", "Tillering / Formative", "Flowering / Grand Growth", "Maturity / Ripening")
    val methods = listOf("Drip Irrigation", "Micro-Sprinkler", "Furrow Irrigation", "Border Strip")

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
                Text("Smart Water & Irrigation Management", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                Text("Calculate field watering requirements, schedules, and water-saving practices", fontSize = 11.sp, color = AgriTextSecondary)
            }
        }

        // Irrigation Inputs Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Crop Selection", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
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

                Text("Growth Stage", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                stages.forEach { st ->
                    val isSelected = selectedStage == st
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFEBF5FF) else Color.Transparent)
                            .clickable { selectedStage = st }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = st,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF2563EB) else AgriTextPrimary
                        )
                    }
                }

                Text("Irrigation Method", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    methods.take(2).forEach { m ->
                        val isSelected = selectedMethod == m
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF2563EB) else Color(0xFFF3F4F6))
                                .clickable { selectedMethod = m }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(m, fontSize = 11.sp, color = if (isSelected) Color.White else AgriTextPrimary)
                        }
                    }
                }

                // Soil Moisture
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Measured Soil Moisture (%):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("$measuredMoisture%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                }
                Slider(
                    value = measuredMoisture.toFloat(),
                    onValueChange = { measuredMoisture = it.toInt() },
                    valueRange = 10f..90f
                )
            }
        }

        // Irrigation Schedule Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recommended Irrigation Schedule", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriDarkGreen)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEBF5FF))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("Estimated Need", color = Color(0xFF1D4ED8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = AgriBorder)

                val (requirementLitres, scheduleDays, durationHours) = when (selectedCrop) {
                    "Sugarcane" -> Triple("22,000 Litres / acre / day", "Every 3 to 4 days", "2.5 hours per drip cycle")
                    "Tomato" -> Triple("12,500 Litres / acre / day", "Every alternate day", "1.5 hours per drip cycle")
                    "Wheat" -> Triple("16,000 Litres / acre / day", "Every 12 to 14 days", "Furrow border wetting")
                    else -> Triple("14,000 Litres / acre / day", "Every 5 days", "2 hours per cycle")
                }

                Text("Water Requirement: $requirementLitres", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AgriTextPrimary)
                Text("Irrigation Frequency: $scheduleDays", fontSize = 12.sp, color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                Text("Pump / Drip Run Time: $durationHours", fontSize = 11.sp, color = AgriTextSecondary)

                Spacer(modifier = Modifier.height(4.dp))

                // Water-saving practices
                Text("Water Conservation Best Practices:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("• Irrigate during early morning or evening to reduce evapotranspiration losses by up to 25%.", fontSize = 11.sp, color = AgriTextSecondary)
                    Text("• Apply 5-7 cm organic crop residue mulch to retain topsoil capillary moisture.", fontSize = 11.sp, color = AgriTextSecondary)
                    Text("• Regularly flush drip lateral lines and maintain 1.2 kg/cm² operating pressure.", fontSize = 11.sp, color = AgriTextSecondary)
                }
            }
        }
    }
}
