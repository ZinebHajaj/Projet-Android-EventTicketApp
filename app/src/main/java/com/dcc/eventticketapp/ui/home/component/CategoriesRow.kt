package com.dcc.eventticketapp.ui.home.component
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dcc.eventticketapp.data.Entities.Category


@Composable
fun CategoriesRow(
    surfaceColor : Color,
    textSecond   : Color,
    selectedCategory : String,
    onCategoryClick  : (String) -> Unit
) {
    val categories = listOf(
        Category("Tous", Icons.Outlined.Apps),
        Category("Concerts", Icons.Outlined.MusicNote),
        Category("Théâtre",  Icons.Outlined.TheaterComedy),
        Category("Sports",   Icons.Outlined.SportsBasketball),
        Category("Ateliers", Icons.Outlined.MenuBook),
        Category("Autres",   Icons.Outlined.MoreHoriz),
    )
    //var selected by remember { mutableStateOf("Concerts") }

    LazyRow(
        modifier              = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { cat ->
            CategoryItem(
                category     = cat,
                isSelected   = selectedCategory == cat.name,
                surfaceColor = surfaceColor,
                textSecond   = textSecond,
                onClick      = { onCategoryClick(cat.name) }
            )
        }
    }
}