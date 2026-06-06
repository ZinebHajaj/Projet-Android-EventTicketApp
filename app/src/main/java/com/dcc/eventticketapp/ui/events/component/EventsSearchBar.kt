package com.dcc.eventticketapp.ui.events.component


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventsSearchBar(
    query        : String,
    onQueryChange: (String) -> Unit,
    surfaceColor : Color,
    textSecond   : Color
) {
    OutlinedTextField(
        value         = query,
        onValueChange = onQueryChange,
        modifier      = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder   = {
            Text(
                "Rechercher un événement...",
                color    = textSecond,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, null,
                tint = textSecond)
        },
        shape  = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = Color(0xFFD4622A),
            unfocusedBorderColor = Color(0xFFEEEEEE),
            focusedContainerColor   = surfaceColor,
            unfocusedContainerColor = surfaceColor,
            cursorColor          = Color(0xFFD4622A)
        ),
        singleLine = true
    )
}