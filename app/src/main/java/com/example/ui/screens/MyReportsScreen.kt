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
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlantScan
import com.example.data.model.SoilReport
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary
import com.example.ui.theme.StatusApprovedGreen
import com.example.ui.theme.StatusRejectedRed

@Composable
fun MyReportsScreen(
    scans: List<PlantScan>,
    soilReports: List<SoilReport>,
    onDeleteScan: (String) -> Unit,
    onDeleteSoilReport: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Plant Scans (${scans.size})", "Soil Health (${soilReports.size})")

    var itemToDeleteId by remember { mutableStateOf<String?>(null) }
    var itemToDeleteType by remember { mutableStateOf("scan") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AgriDarkGreen)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("My Agricultural Reports", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                Text("Archived crop disease scans, diagnostic recommendations, and soil testing cards", fontSize = 11.sp, color = AgriTextSecondary)
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = AgriDarkGreen,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AgriDarkGreen
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            if (selectedTab == 0) {
                // Plant Scans
                if (scans.isEmpty()) {
                    EmptyReportsView("No saved plant scans yet.")
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        scans.forEach { scan ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Crop: ${scan.cropName}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(scan.diseaseName, fontSize = 12.sp, color = if (scan.isHealthy) StatusApprovedGreen else StatusRejectedRed, fontWeight = FontWeight.Bold)
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (scan.isHealthy) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                            ) {
                                                Text("${scan.confidenceScore}% Confidence", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }

                                            IconButton(
                                                onClick = {
                                                    itemToDeleteId = scan.id
                                                    itemToDeleteType = "scan"
                                                }
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }

                                    HorizontalDivider(color = AgriBorder, modifier = Modifier.padding(vertical = 8.dp))

                                    Text("Symptoms: ${scan.symptoms}", fontSize = 11.sp, color = AgriTextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Treatment: ${scan.recommendedSteps}", fontSize = 11.sp, color = AgriTextPrimary)

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Saved on: ${scan.dateFormatted}", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            } else {
                // Soil Reports
                if (soilReports.isEmpty()) {
                    EmptyReportsView("No saved soil test reports yet.")
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        soilReports.forEach { soil ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Soil Report: ${soil.cropName}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("${soil.soilType} • pH ${soil.ph}", fontSize = 12.sp, color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                                        }

                                        IconButton(
                                            onClick = {
                                                itemToDeleteId = soil.id
                                                itemToDeleteType = "soil"
                                            }
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    HorizontalDivider(color = AgriBorder, modifier = Modifier.padding(vertical = 8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("N: ${soil.nitrogen}", fontSize = 10.sp, color = AgriTextSecondary)
                                        Text("P: ${soil.phosphorus}", fontSize = 10.sp, color = AgriTextSecondary)
                                        Text("K: ${soil.potassium}", fontSize = 10.sp, color = AgriTextSecondary)
                                        Text("Moisture: ${soil.moisturePercent}%", fontSize = 10.sp, color = AgriTextSecondary)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Diagnosis: ${soil.summary}", fontSize = 11.sp, color = AgriTextPrimary)
                                    Text("Amendments: ${soil.recommendations}", fontSize = 11.sp, color = AgriTextSecondary)

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Date: ${soil.dateFormatted}", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog before deleting
    if (itemToDeleteId != null) {
        AlertDialog(
            onDismissRequest = { itemToDeleteId = null },
            title = { Text("Delete Report", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to permanently delete this report record?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = itemToDeleteId!!
                        if (itemToDeleteType == "scan") {
                            onDeleteScan(id)
                        } else {
                            onDeleteSoilReport(id)
                        }
                        itemToDeleteId = null
                    }
                ) {
                    Text("Delete", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDeleteId = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EmptyReportsView(msg: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(msg, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("Reports saved from Plant Scanner or Soil Health appear here.", fontSize = 11.sp, color = Color.Gray)
        }
    }
}
