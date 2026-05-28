package com.dcc.eventticketapp.ui.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.BorderColor
import com.dcc.eventticketapp.ui.theme.ErrorLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    labelFontSize: TextUnit = 16.sp,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,

    /* --- Keyboard ----- */
    keyboardType: KeyboardType = KeyboardType.Text,

    /* --- Password ----- */
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {},

    /* --- Erreur ----- */
    isError: Boolean = false,
    errorMessage: String = "",
) {
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        label           = {
            Text(
                text = label,
                fontSize = labelFontSize
            )
        },
        leadingIcon     = {
            Icon(
                imageVector        = leadingIcon,
                contentDescription = null,
                tint               = OrangeMain
            )
        },

        // icône oeil uniquement si c'est un champ password
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint               = TextGrayMode
                    )
                }
            }
        } else null,

        // masquage uniquement si c'est un champ password et non visible
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,

        isError         = isError,
        supportingText  = {
            Text(
                text  = if (isError) errorMessage else "",
                color = ErrorLight
            )
        },

        singleLine      = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier        = modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(12.dp),
        colors          = OutlinedTextFieldDefaults.colors(
            focusedBorderColor        = OrangeMain,
            unfocusedBorderColor      = BorderColor,
            focusedLabelColor         = OrangeMain,
            unfocusedLabelColor       = TextGrayMode,
            cursorColor               = OrangeMain,
            focusedLeadingIconColor   = OrangeMain,
            unfocusedLeadingIconColor = TextGrayMode,
            errorBorderColor          = ErrorLight,
            errorLabelColor           = ErrorLight,
            focusedContainerColor     = Color.White,
            unfocusedContainerColor   = Color.White,
        )
    )
}