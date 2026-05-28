package com.dcc.eventticketapp.ui.auth.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

import com.dcc.eventticketapp.ui.auth.components.AuthTextField
import com.dcc.eventticketapp.ui.auth.components.AuthButton
import com.dcc.eventticketapp.ui.auth.components.AuthFooterLink
import com.dcc.eventticketapp.ui.auth.components.AuthHeader

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var fullName        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible        by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }
    var isLoading              by remember { mutableStateOf(false) }

    // Erreurs
    var emailError           by remember { mutableStateOf(false) }
    var phoneError           by remember { mutableStateOf(false) }
    var passwordMatchError   by remember { mutableStateOf(false) }

    // Champs valides
    val isFormValid = fullName.isNotBlank()
            && email.isNotBlank()
            && !emailError
            && phone.isNotBlank()
            && !phoneError
            && password.isNotBlank()
            && confirmPassword.isNotBlank()
            && !passwordMatchError
            && acceptedTerms
            && !isLoading

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(OrangeMain, OrangeLight)
                    )
                )
            .verticalScroll(rememberScrollState())
            .padding(vertical = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 48.dp, bottom = 48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /* ------------ Header --------- */
                AuthHeader(
                    title    = "Inscription",
                    subtitle = "Créez votre compte pour continuer"
                )

                Spacer(modifier = Modifier.height(20.dp))

                /* ------------ Nom complet --------- */
                AuthTextField(
                    value         = fullName,
                    onValueChange = { fullName = it },
                    label         = "Nom complet",
                    leadingIcon   = Icons.Default.Person,
                    keyboardType  = KeyboardType.Text
                )

                /* ------------ Email --------- */
                AuthTextField(
                    value         = email,
                    onValueChange = {
                        email = it
                        emailError = it.isNotEmpty() &&
                                !Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    label        = "Adresse email",
                    leadingIcon  = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    isError      = emailError,
                    errorMessage = "Adresse email invalide"
                )

                /* ------------ Téléphone --------- */
                AuthTextField(
                    value         = phone,
                    onValueChange = {
                        phone = it
                        phoneError = it.isNotEmpty() && it.length < 10
                    },
                    label        = "Téléphone",
                    leadingIcon  = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    isError      = phoneError,
                    errorMessage = "Numéro invalide (min. 10 chiffres)"
                )

                /* ------------ Mot de passe --------- */
                AuthTextField(
                    value              = password,
                    onValueChange      = {
                        password = it
                        passwordMatchError = confirmPassword.isNotEmpty() && it != confirmPassword
                    },
                    label              = "Mot de passe",
                    leadingIcon        = Icons.Default.Lock,
                    isPassword         = true,
                    passwordVisible    = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )

                /* ------------ Confirmer mot de passe --------- */
                AuthTextField(
                    value              = confirmPassword,
                    onValueChange      = {
                        confirmPassword = it
                        passwordMatchError = it.isNotEmpty() && it != password
                    },
                    label              = "Confirmer mot de passe",
                    labelFontSize      = 14.sp,
                    leadingIcon        = Icons.Default.Lock,
                    isPassword         = true,
                    passwordVisible    = confirmPasswordVisible,
                    onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible },
                    isError            = passwordMatchError,
                    errorMessage       = "Les mots de passe ne correspondent pas"
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* ------------ Checkbox Conditions --------- */
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked         = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        colors          = CheckboxDefaults.colors(
                            checkedColor   = OrangeMain,
                            uncheckedColor = TextGrayMode
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        buildAnnotatedString {
                            append("J'accepte les ")
                            withStyle(SpanStyle(color = OrangeMain, fontWeight = FontWeight.SemiBold)) {
                                append("Conditions d'utilisation")
                            }
                            append(" et la ")
                            withStyle(SpanStyle(color = OrangeMain, fontWeight = FontWeight.SemiBold)) {
                                append("Politique de confidentialité")
                            }
                        },
                        fontSize = 13.sp,
                        color    = TextGrayMode
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                /* ------------ Bouton S'inscrire --------- */
                AuthButton(
                    text      = "S'inscrire",
                    onClick   = { isLoading = true; onRegisterClick() },
                    enabled   = isFormValid,
                    isLoading = isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* ------------ Lien Connexion --------- */
                AuthFooterLink(
                    question      = "Déjà un compte ? ",
                    actionText    = "Se connecter",
                    onActionClick = onNavigateToLogin
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    EventTicketAppTheme {
        RegisterScreen()
    }
}