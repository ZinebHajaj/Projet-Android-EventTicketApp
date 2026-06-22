package com.dcc.eventticketapp.ui.booking.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.utils.CurrencyConverter

@Composable
fun CurrencySelector(
    selectedCurrency : String,
    onCurrencyChange : (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            modifier = Modifier.clickable { expanded = true },
            shape    = RoundedCornerShape(20.dp),
            color    = OrangeMain.copy(alpha = 0.12f)
        ) {
            Row(
                modifier          = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text       = selectedCurrency,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = OrangeMain
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Changer la devise",
                    tint     = OrangeMain,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CurrencyConverter.availableCurrencies().forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onCurrencyChange(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}