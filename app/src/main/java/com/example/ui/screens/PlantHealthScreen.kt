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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.PlantScan
import com.example.ui.ScreenDestination
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenAccent
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary
import com.example.ui.theme.StatusApprovedGreen
import com.example.ui.theme.StatusRejectedRed

@Composable
fun PlantHealthScreen(
    savedScans: List<PlantScan>,
    onOpenScanner: () -> Unit,
    onOpenReports: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCrop by remember { mutableStateOf("Tomato") }
    val crops = listOf("Tomato", "Wheat", "Cotton", "Soybean", "Rice")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AgriDarkGreen)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("Plant Health Diagnostic Hub", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                Text("Check disease symptoms, causes, preventive treatment, and fertilizer guidance", fontSize = 11.sp, color = AgriTextSecondary)
            }
        }

        // Action CTA: Scan or View Reports
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onOpenScanner,
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).testTag("open_scanner_action")
            ) {
                Icon(Icons.Default.CropFree, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Scan New Plant", fontSize = 12.sp, color = Color.White)
            }

            OutlinedButton(
                onClick = onOpenReports,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("My Reports (${savedScans.size})", fontSize = 12.sp)
            }
        }

        // Crop Selection
        Text("Select Crop to View Health Protocols", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AgriTextPrimary)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            crops.forEach { c ->
                val isSelected = selectedCrop == c
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) AgriDarkGreen else Color.White)
                        .border(1.dp, if (isSelected) AgriDarkGreen else AgriBorder, RoundedCornerShape(14.dp))
                        .clickable { selectedCrop = c }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = c,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else AgriTextPrimary
                    )
                }
            }
        }

        // Crop Health Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "$selectedCrop Health & Disease Management",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = AgriDarkGreen
                )

                Spacer(modifier = Modifier.height(10.dp))

                when (selectedCrop) {
                    "Tomato" -> CropDetailInfo(
                        commonDisease = "Early Blight & Tomato Leaf Curl Virus",
                        symptoms = "Circular target-board spots on lower foliage; upward curling and puckering of young leaves with stunting.",
                        causes = "Alternaria solani fungal spores during humid cloudy weather; Whitefly (Bemisia tabaci) insect vector.",
                        treatment = "Foliar spray of Mancozeb 75 WP (2.5g/L) or Copper Oxychloride. Use yellow sticky traps for whiteflies.",
                        fertilizer = "Apply balanced NPK (19:19:19) via drip fertigation. Supply Calcium Nitrate to prevent blossom end rot.",
                        watering = "Adopt drip irrigation; avoid wet foliage in the evening."
                    )
                    "Wheat" -> CropDetailInfo(
                        commonDisease = "Yellow Rust & Powdery Mildew",
                        symptoms = "Bright yellow pustules aligned in linear stripes on leaf blades; white powdery fungal patches.",
                        causes = "Puccinia striiformis spores carried by cool north winds and high relative humidity (>85%).",
                        treatment = "Apply Propiconazole 25% EC @ 1ml/L at initial symptom onset. Ensure field drainage.",
                        fertilizer = "Avoid excessive nitrogen in second top dressing; supply Potash (MOP) to boost fungal resistance.",
                        watering = "Irrigate at critical stages: Crown root initiation and flowering stage."
                    )
                    "Cotton" -> CropDetailInfo(
                        commonDisease = "Bacterial Blight & Grey Mildew",
                        symptoms = "Angular water-soaked leaf spots turning dark brown; white powdery growth on lower leaf surfaces.",
                        causes = "Xanthomonas citri pv. malvacearum bacteria splashing through raindrops; warm humid microclimate.",
                        treatment = "Copper Oxychloride (30g) + Streptocycline (1g) in 10L water. Remove infected plant ratoon.",
                        fertilizer = "Apply Potassium Nitrate (13:0:45) foliar spray @ 10g/L during boll development.",
                        watering = "Avoid water logging; furrow irrigation every 12-15 days based on soil moisture."
                    )
                    else -> CropDetailInfo(
                        commonDisease = "Leaf Spot & Stem Rot",
                        symptoms = "Irregular necrosis on leaf margins, yellowing of foliage and wilting.",
                        causes = "Soil-borne fungal pathogens; continuous monocropping without rotation.",
                        treatment = "Seed treatment with Trichoderma viride (4g/kg seed); foliar Carbendazim 50% WP spray.",
                        fertilizer = "Soil application of organic compost and micronutrient mix.",
                        watering = "Maintain field capacity without pooling water."
                    )
                }
            }
        }

        // Previous scan records
        if (savedScans.isNotEmpty()) {
            Text("Previous Saved Scans", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriTextPrimary)
            savedScans.take(3).forEach { scan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, AgriBorder, RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${scan.cropName} - ${scan.diseaseName}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AgriTextPrimary)
                            Text(scan.dateFormatted, fontSize = 10.sp, color = AgriTextSecondary)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (scan.isHealthy) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (scan.isHealthy) "Healthy" else "Diseased",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (scan.isHealthy) StatusApprovedGreen else Color(0xFFD97706)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CropDetailInfo(
    commonDisease: String,
    symptoms: String,
    causes: String,
    treatment: String,
    fertilizer: String,
    watering: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        DetailRow("Common Disease:", commonDisease)
        DetailRow("Symptoms:", symptoms)
        DetailRow("Possible Causes:", causes)
        DetailRow("Recommended Treatment:", treatment)
        DetailRow("Fertilizer Guidance:", fertilizer)
        DetailRow("Watering Guidance:", watering)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column {
        Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = AgriDarkGreen)
        Text(text = value, fontSize = 11.sp, color = AgriTextSecondary, lineHeight = 15.sp)
    }
}
