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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
fun SoilHealthScreen(
    onSaveReport: (crop: String, soilType: String, ph: Double, n: String, p: String, k: String, moisture: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var cropName by remember { mutableStateOf("Wheat") }
    var soilType by remember { mutableStateOf("Medium Black Soil") }
    var soilPh by remember { mutableDoubleStateOf(7.2) }
    var nitrogenLevel by remember { mutableStateOf("Medium (270 kg/ha)") }
    var phosphorusLevel by remember { mutableStateOf("High (42 kg/ha)") }
    var potassiumLevel by remember { mutableStateOf("High (310 kg/ha)") }
    var moisturePercent by remember { mutableIntStateOf(45) }
    var uploadedFileName by remember { mutableStateOf<String?>(null) }
    var hasCalculatedSummary by remember { mutableStateOf(true) }

    val soilTypes = listOf("Medium Black Soil", "Deep Black Cotton", "Red Loamy", "Alluvial", "Sandy Loam")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AgriDarkGreen)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("Soil Health & Soil-Test Analysis", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                Text("Enter measured laboratory parameters or upload soil testing card", fontSize = 11.sp, color = AgriTextSecondary)
            }
        }

        // Scientific Notice Banner (MANDATORY per rule 8)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFEF3C7))
                .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Laboratory Accuracy Note", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF92400E))
                Text(
                    text = "A smartphone camera photo cannot reliably measure pH, NPK, or moisture. Enter verified laboratory test values or obtain a Government Soil Health Card from your local agricultural extension office.",
                    fontSize = 11.sp,
                    color = Color(0xFF78350F),
                    lineHeight = 14.sp
                )
            }
        }

        // Soil Test Entry Form
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Soil Parameters Entry", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriTextPrimary)

                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it },
                    label = { Text("Planned Crop (e.g. Wheat, Sugarcane, Cotton)", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Soil Classification Type", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AgriTextPrimary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    soilTypes.take(3).forEach { st ->
                        val isSelected = soilType == st
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AgriDarkGreen else Color(0xFFF3F4F6))
                                .clickable { soilType = st }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(st, fontSize = 10.sp, color = if (isSelected) Color.White else AgriTextPrimary)
                        }
                    }
                }

                // Soil pH Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Measured Soil pH:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(String.format(java.util.Locale.US, "%.1f", soilPh), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AgriDarkGreen)
                    }
                    Slider(
                        value = soilPh.toFloat(),
                        onValueChange = { soilPh = it.toDouble() },
                        valueRange = 4.0f..10.0f,
                        steps = 59
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("4.0 (Very Acidic)", fontSize = 9.sp, color = AgriTextSecondary)
                        Text("7.0 (Neutral)", fontSize = 9.sp, color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                        Text("10.0 (Very Alkaline)", fontSize = 9.sp, color = AgriTextSecondary)
                    }
                }

                // Measured NPK
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nitrogenLevel,
                        onValueChange = { nitrogenLevel = it },
                        label = { Text("Nitrogen (N)", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = phosphorusLevel,
                        onValueChange = { phosphorusLevel = it },
                        label = { Text("Phosphorus (P)", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = potassiumLevel,
                        onValueChange = { potassiumLevel = it },
                        label = { Text("Potassium (K)", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Moisture percentage
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Field Soil Moisture (%):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("$moisturePercent%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                }
                Slider(
                    value = moisturePercent.toFloat(),
                    onValueChange = { moisturePercent = it.toInt() },
                    valueRange = 0f..100f
                )

                // Upload Soil Card document
                OutlinedButton(
                    onClick = {
                        uploadedFileName = "Soil_Test_Report_Oct2026.pdf"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (uploadedFileName != null) "Uploaded: $uploadedFileName" else "Upload Soil Test Report (PDF/Scan)",
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Soil Health Summary and Crop Recommendations
        if (hasCalculatedSummary) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Soil Diagnostic Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriDarkGreen)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("Measured Values", color = Color(0xFF15803D), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(color = AgriBorder)

                    val condition = when {
                        soilPh < 6.5 -> "Acidic condition: Phosphorus and molybdenum fixation likely. Magnesium deficiency risk."
                        soilPh > 7.8 -> "Calcareous Alkaline condition: Free Calcium Carbonate binds phosphorus; Zinc and Iron micronutrient deficiencies."
                        else -> "Optimal neutral reaction: Ideal bioavailability for macronutrients and biological nitrogen fixation."
                    }

                    Text("Condition Assessment:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                    Text(condition, fontSize = 11.sp, color = AgriTextSecondary, lineHeight = 15.sp)

                    Text("Tailored Amendments for $cropName:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriTextPrimary)
                    val amendment = when {
                        soilPh < 6.5 -> "Apply Agricultural Dolomite Lime @ 200 kg/acre. Supplement Magnesium Sulphate."
                        soilPh > 7.8 -> "Apply Agricultural Gypsum @ 150 kg/acre. Incorporate green manure (Sesbania) to reduce pH."
                        else -> "Apply well-decomposed FYM @ 4 tonnes/acre with biofertilizers (Azotobacter + PSB)."
                    }
                    Text(amendment, fontSize = 11.sp, color = AgriTextSecondary, lineHeight = 15.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            onSaveReport(cropName, soilType, soilPh, nitrogenLevel, phosphorusLevel, potassiumLevel, moisturePercent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_soil_report_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Soil Health Report", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
