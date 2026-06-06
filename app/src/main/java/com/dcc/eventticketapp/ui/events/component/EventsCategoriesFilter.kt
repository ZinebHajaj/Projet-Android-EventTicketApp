package com.dcc.eventticketapp.ui.events.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun EventsCategoriesFilter(
    categories       : List<String>,
    selectedCategory : String,
    onCategoryClick  : (String) -> Unit
) {
    LazyRow(
        modifier              = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick  = { onCategoryClick(category) },
                label    = { Text(category, fontSize = 13.sp) },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = OrangeMain,
                    selectedLabelColor     = Color.White
                )
            )
        }
    }
}