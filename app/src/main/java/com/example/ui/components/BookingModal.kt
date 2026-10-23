package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.Equipment
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenAccent
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextMuted
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary
import com.example.ui.theme.RatingGold

@Composable
fun BookingModal(
    equipment: Equipment,
    onDismiss: () -> Unit,
    onSubmitBooking: (equipment: Equipment, startDate: String, durationDays: Int, notes: String) -> Unit
) {
    var durationDays by remember { mutableIntStateOf(1) }
    var startDate by remember { mutableStateOf("Tomorrow (Oct 24, 2026)") }
    var notes by remember { mutableStateOf("") }

    val totalCost = equipment.dailyRate * durationDays

    val imageRes = when (equipment.imageDrawableName) {
        "equip_rotavator" -> R.drawable.equip_rotavator
        "equip_harvester" -> R.drawable.equip_harvester
        "equip_seed_drill" -> R.drawable.equip_seed_drill
        else -> R.drawable.equip_tractor
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("equipment_booking_modal"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = equipment.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                    )
                    // Close button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Available Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AgriGreenAccent)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (equipment.isAvailable) "Available" else "Rented",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Details Content
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = equipment.title,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = equipment.subtitle.ifBlank { "${equipment.category} • ${equipment.hp}" },
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }

                        Text(
                            text = "₹${equipment.dailyRate} / day",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriDarkGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Location & Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AgriTextSecondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${equipment.location} • ${equipment.distanceKm} km",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = RatingGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${equipment.rating} (${equipment.reviewCount} reviews)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = AgriTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = AgriBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Owner details
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF9FAFB))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Equipment Owner: ${equipment.ownerName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AgriTextPrimary
                            )
                            Text(
                                text = "Contact: ${equipment.ownerPhone}",
                                fontSize = 11.sp,
                                color = AgriTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Booking Duration Selection
                    Text(
                        text = "Rental Duration (Days)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, AgriBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$durationDays ${if (durationDays == 1) "Day" else "Days"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = AgriTextPrimary
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (durationDays > 1) durationDays-- },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease Days")
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(
                                onClick = { if (durationDays < 30) durationDays++ },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase Days")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Special Notes
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Special requirements or field address notes", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Calculation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Estimated Rental",
                                fontSize = 12.sp,
                                color = Color(0xFF1E3A2B)
                            )
                            Text(
                                text = "₹${equipment.dailyRate} × $durationDays days",
                                fontSize = 11.sp,
                                color = AgriTextSecondary
                            )
                        }

                        Text(
                            text = "₹$totalCost",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriDarkGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm button
                    Button(
                        onClick = {
                            onSubmitBooking(equipment, startDate, durationDays, notes)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_booking_button")
                    ) {
                        Text(
                            text = "Confirm & Request Booking",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
