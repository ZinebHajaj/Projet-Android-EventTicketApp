package com.dcc.eventticketapp.ui.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@Composable
fun AuthFooterLink(
    question: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text     = question,
            color    = TextGrayMode,
            fontSize = 14.sp
        )
        Text(
            text       = actionText,
            color      = OrangeMain,
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier   = Modifier.clickable { onActionClick() }
        )
    }
}