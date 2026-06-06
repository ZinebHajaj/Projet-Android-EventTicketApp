package com.dcc.eventticketapp.ui.home.component
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun BottomNavBar(
    surfaceColor   : Color,
    textSecond     : Color,
    onProfileClick : () -> Unit,
    onEventsClick  : () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        "Accueil" to Icons.Outlined.Home,
        "Événements"  to Icons.Outlined.ConfirmationNumber,
        "Favoris" to Icons.Outlined.FavoriteBorder,
        "Billets" to Icons.Outlined.QrCode,
        "Profil"  to Icons.Outlined.Person,
    )

    NavigationBar(
        containerColor = surfaceColor,
        tonalElevation = 8.dp
    ) {
        tabs.forEachIndexed { index, (label, icon) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick  = {
                    selectedTab = index
                    when (index) {
                        1 -> onEventsClick()
                        4 -> onProfileClick()
                    }
                },

                icon  = {
                    Icon(icon, contentDescription = label,
                        modifier = Modifier.size(22.dp))
                },
                label = {
                    Text(label, fontSize = 9.sp, maxLines = 1)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = OrangeMain,
                    selectedTextColor   = OrangeMain,
                    unselectedIconColor = textSecond,
                    unselectedTextColor = textSecond,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}