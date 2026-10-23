package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.theme.AgriBackground
import com.example.ui.theme.AgriBorder
import com.example.ui.theme.AgriDarkGreen
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriTextPrimary
import com.example.ui.theme.AgriTextSecondary

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    onUpdateProfile: (UserProfile) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }

    var fullName by remember(userProfile) { mutableStateOf(userProfile?.fullName ?: "Om Jaysingpure") }
    var role by remember(userProfile) { mutableStateOf(userProfile?.role ?: "Student") }
    var email by remember(userProfile) { mutableStateOf(userProfile?.email ?: "omjaysingpure2@gmail.com") }
    var phone by remember(userProfile) { mutableStateOf(userProfile?.phone ?: "+91 98765 43210") }
    var location by remember(userProfile) { mutableStateOf(userProfile?.location ?: "Pune, Maharashtra") }
    var village by remember(userProfile) { mutableStateOf(userProfile?.village ?: "Haveli") }
    var district by remember(userProfile) { mutableStateOf(userProfile?.district ?: "Pune") }
    var state by remember(userProfile) { mutableStateOf(userProfile?.state ?: "Maharashtra") }
    var farmAreaAcres by remember(userProfile) { mutableStateOf(userProfile?.farmAreaAcres ?: "5.5") }
    var age by remember(userProfile) { mutableStateOf(userProfile?.age ?: "23") }

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
                Text("Farmer & User Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriDarkGreen)
                Text("Manage personal credentials, role, and landholding records", fontSize = 11.sp, color = AgriTextSecondary)
            }
        }

        // Profile Avatar Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_avatar),
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, AgriGreenPrimary, CircleShape)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = fullName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriTextPrimary
                )

                Text(
                    text = "$role • $location",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { isEditing = !isEditing },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isEditing) AgriGreenPrimary else AgriDarkGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("edit_profile_toggle_button")
                    ) {
                        Icon(if (isEditing) Icons.Default.Check else Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEditing) "Done Editing" else "Edit Profile", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }

        // Profile Details Fields
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AgriBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Account & Land Details", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriTextPrimary)

                if (isEditing) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth().testTag("profile_input_name")
                    )
                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Role (Student / Farmer / Equipment Owner)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("District & State Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = farmAreaAcres,
                        onValueChange = { farmAreaAcres = it },
                        label = { Text("Farm Area (Acres)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val updated = (userProfile ?: UserProfile()).copy(
                                fullName = fullName,
                                role = role,
                                email = email,
                                phone = phone,
                                location = location,
                                village = village,
                                district = district,
                                state = state,
                                farmAreaAcres = farmAreaAcres,
                                age = age
                            )
                            onUpdateProfile(updated)
                            isEditing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgriDarkGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("save_profile_button")
                    ) {
                        Text("Save Profile Changes", color = Color.White, fontSize = 13.sp)
                    }
                } else {
                    ProfileFieldRow("Full Name", fullName)
                    HorizontalDivider(color = AgriBorder)
                    ProfileFieldRow("Role", role)
                    HorizontalDivider(color = AgriBorder)
                    ProfileFieldRow("Email Address", email)
                    HorizontalDivider(color = AgriBorder)
                    ProfileFieldRow("Phone Number", phone)
                    HorizontalDivider(color = AgriBorder)
                    ProfileFieldRow("Location", location)
                    HorizontalDivider(color = AgriBorder)
                    ProfileFieldRow("Farm Land Area", "$farmAreaAcres Acres")
                    HorizontalDivider(color = AgriBorder)
                    ProfileFieldRow("Age", "$age years")
                }
            }
        }
    }
}

@Composable
fun ProfileFieldRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = AgriTextSecondary)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AgriTextPrimary)
    }
}
