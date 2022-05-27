package com.ericg.jichat.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectName() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Welcome to JiChat",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 54.dp)
        )

        var userName by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = userName,
                onValueChange = { newValue ->
                    if (newValue.length <= 12) {
                        userName = newValue
                    }
                },
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter your username")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "icon")
                }
            )

            Text(text = "Select Your Avatar")
            LazyRow(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val icons = listOf<Int>()
                items(10) {
                    AvatarItem(vector = Icons.Default.Face)
                }
            }

            Text(text = "Select Bot's Avatar")
            LazyRow(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val icons = listOf<Int>()
                items(10) {
                    AvatarItem(vector = Icons.Default.Face)
                }
            }
        }

        Button(modifier = Modifier
            .fillMaxWidth(),
            shape = CircleShape,
            onClick = {

            }) {
            Text(text = "Proceed", fontSize = 16.sp)
        }
    }
}

@Composable
fun AvatarItem(
    selected: Boolean = false,
    vector: ImageVector
) {
    var isSelected by remember { mutableStateOf(selected) }
    Image(
        imageVector = vector,
        modifier = Modifier.padding(4.dp)
            .sizeIn(minHeight = 54.dp, minWidth = 54.dp).clip(CircleShape)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = if (isSelected) Color.LightGray else Color.Transparent
            )
            .clickable {
                isSelected = !isSelected
            }
            ,
        contentDescription = "avatar"
    )
}

@Preview(device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun SelectNamePrev() {
    SelectName()
}
