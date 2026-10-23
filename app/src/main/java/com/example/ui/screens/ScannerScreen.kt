package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.R
import com.example.data.model.PlantScan
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenAccent
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextMuted
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary
import com.example.ui.theme.StatusApprovedGreen
import com.example.ui.theme.StatusRejectedRed

@Composable
fun ScannerScreen(
    scannedResult: PlantScan?,
    isAnalyzing: Boolean,
    onAnalyze: (cropGuess: String, symptomType: Int) -> Unit,
    onSaveReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isCameraActive by remember { mutableStateOf(false) }
    var capturedPhotoReady by remember { mutableStateOf(false) }
    var permissionDeniedMessage by remember { mutableStateOf<String?>(null) }
    var selectedCropName by remember { mutableStateOf("Tomato") }
    var scanVariantCounter by remember { mutableIntStateOf(0) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            permissionDeniedMessage = null
            isCameraActive = true
        } else {
            permissionDeniedMessage = "Camera permission was denied. You can still upload a crop photo from gallery or enter symptoms."
        }
    }

    // Photo picker launcher (zero-permission standard Android Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            capturedPhotoReady = true
            isCameraActive = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AgriBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AI Plant Scanner & Health Diagnostic",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriDarkGreen
                )
                Text(
                    text = "Capture leaves or crop parts to diagnose diseases and receive agronomic treatment",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )
            }
        }

        // Camera / Image Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(16.dp))
                .testTag("scanner_viewfinder_card"),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isCameraActive) {
                    // Live Viewfinder Mode
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Viewfinder Reticle
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .border(2.dp, Color(0xFF4ADE80), RoundedCornerShape(16.dp))
                        )

                        Text(
                            text = "Align infected leaf inside frame",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { isCameraActive = false },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                capturedPhotoReady = true
                                isCameraActive = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("capture_photo_button")
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Capture Photo", fontSize = 12.sp, color = Color.White)
                        }
                    }
                } else if (capturedPhotoReady) {
                    // Captured Preview Mode
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AgriGreenPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Leaf Photo Captured & Ready for AI Analysis",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = AgriTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                capturedPhotoReady = false
                                isCameraActive = true
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retake", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                scanVariantCounter++
                                onAnalyze(selectedCropName, scanVariantCounter)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isAnalyzing,
                            modifier = Modifier.testTag("analyze_plant_button")
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analyzing Leaf...", fontSize = 12.sp, color = Color.White)
                            } else {
                                Icon(Icons.Default.CropFree, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analyze Plant", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                } else {
                    // Default State: Prompt to Open Camera or Upload
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2F1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CropFree,
                            contentDescription = null,
                            tint = Color(0xFF00897B),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Instant Crop Disease Diagnosis",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriTextPrimary
                    )

                    Text(
                        text = "Take a clear, well-lit photo of leaves, stems, or fruits showing visible symptoms",
                        fontSize = 11.sp,
                        color = AgriTextSecondary,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                if (hasCameraPermission) {
                                    isCameraActive = true
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("open_camera_button")
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Camera", fontSize = 12.sp, color = Color.White)
                        }

                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("upload_gallery_button")
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload from Gallery", fontSize = 12.sp)
                        }
                    }

                    if (permissionDeniedMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = permissionDeniedMessage!!,
                            color = StatusRejectedRed,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Target Crop Selector
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Target Crop",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AgriTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Tomato", "Wheat", "Cotton", "Soybean", "Rice").forEach { crop ->
                        val isSelected = selectedCropName == crop
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) AgriGreenPrimary else Color(0xFFF3F4F6))
                                .clickable { selectedCropName = crop }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = crop,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else AgriTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Analysis Results Card (when analyzed)
        if (scannedResult != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, AgriBorder, RoundedCornerShape(16.dp))
                    .testTag("scan_result_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Header Result
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Crop: ${scannedResult.cropName}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = scannedResult.diseaseName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (scannedResult.isHealthy) StatusApprovedGreen else StatusRejectedRed
                            )
                        }

                        // Confidence Score Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (scannedResult.isHealthy) Color(0xFFDCFCE7) else Color(0xFFFEF3C7))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${scannedResult.confidenceScore}% Confidence",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (scannedResult.isHealthy) StatusApprovedGreen else Color(0xFFD97706)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = AgriBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Symptoms
                    Text("Visible Symptoms", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AgriTextPrimary)
                    Text(scannedResult.symptoms, fontSize = 12.sp, color = AgriTextSecondary, lineHeight = 16.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Recommended Next Steps
                    Text("Recommended Treatment", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AgriTextPrimary)
                    Text(scannedResult.recommendedSteps, fontSize = 12.sp, color = AgriTextSecondary, lineHeight = 16.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fertilizer & Watering
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF9FAFB))
                                .padding(10.dp)
                        ) {
                            Text("Fertilizer Guidance", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = AgriDarkGreen)
                            Text(scannedResult.fertilizerGuidance, fontSize = 11.sp, color = AgriTextSecondary)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF9FAFB))
                                .padding(10.dp)
                        ) {
                            Text("Watering Guidance", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color(0xFF1D4ED8))
                            Text(scannedResult.wateringGuidance, fontSize = 11.sp, color = AgriTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Notice regarding uncertainty
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFFBEB))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI diagnosis is advisory. Verify symptoms with local Krishi Vigyan Kendra (KVK) for severe outbreaks.",
                            fontSize = 10.sp,
                            color = Color(0xFF92400E)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onSaveReport,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("save_scan_report_button")
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Report", fontSize = 12.sp, color = Color.White)
                        }

                        OutlinedButton(
                            onClick = {
                                capturedPhotoReady = false
                                isCameraActive = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Scan Again", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
