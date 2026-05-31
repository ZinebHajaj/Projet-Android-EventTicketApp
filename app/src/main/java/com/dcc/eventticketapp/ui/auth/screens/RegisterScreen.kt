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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.R
import com.dcc.eventticketapp.ui.auth.AuthIntent
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

import com.dcc.eventticketapp.ui.auth.components.AuthTextField
import com.dcc.eventticketapp.ui.auth.components.AuthButton
import com.dcc.eventticketapp.ui.auth.components.AuthFooterLink
import com.dcc.eventticketapp.ui.auth.components.AuthHeader
import com.dcc.eventticketapp.ui.theme.ErrorLight

@Composable
fun RegisterScreen(
    viewModel : AuthViewModel,
    onRegisterSuccess : () -> Unit = {},
    onNavigateToLogin : () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.handleIntent(AuthIntent.ResetState)
            onRegisterSuccess()
        }
    }

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
                    title    = stringResource(R.string.register_title),
                    subtitle = stringResource(R.string.register_subtitle)

                )

                Spacer(modifier = Modifier.height(20.dp))

                /* ------------ Nom complet --------- */
                AuthTextField(
                    value = state.registerName,
                    onValueChange = {
                        viewModel.handleIntent(AuthIntent.RegisterNameChanged(it))
                    },
                    label = stringResource(R.string.register_name),
                    leadingIcon   = Icons.Default.Person,
                    keyboardType  = KeyboardType.Text
                )

                /* ------------ Email --------- */
                AuthTextField(
                    value = state.registerEmail,
                    onValueChange = {
                        viewModel.handleIntent(AuthIntent.RegisterEmailChanged(it))
                    },
                    label = stringResource(R.string.register_email),
                    leadingIcon  = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    isError      = state.emailError,
                    errorMessage = stringResource(R.string.error_invalid_email)
                )

                /* ------------ Téléphone --------- */
                AuthTextField(
                    value = state.registerPhone,
                    onValueChange = {
                        viewModel.handleIntent(AuthIntent.RegisterPhoneChanged(it))
                    },
                    label = stringResource(R.string.register_phone),
                    leadingIcon  = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    isError = state.phoneError,
                    errorMessage = stringResource(R.string.error_invalid_phone)
                )

                /* ------------ Mot de passe --------- */
                AuthTextField(
                    value = state.registerPassword,
                    onValueChange = {
                        viewModel.handleIntent(AuthIntent.RegisterPasswordChanged(it))
                    },
                    label = stringResource(R.string.register_password),
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )

                /* ------------ Confirmer mot de passe --------- */
                AuthTextField(
                    value = state.registerConfirmPassword,
                    onValueChange = {
                        viewModel.handleIntent(AuthIntent.RegisterConfirmPasswordChanged(it))
                    },
                    label = stringResource(R.string.register_confirm_password),
                    labelFontSize = 14.sp,
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = confirmPasswordVisible,
                    onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible },
                    isError = state.passwordMatchError,
                    errorMessage = stringResource(R.string.error_password_mismatch)
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* ------------ Checkbox Conditions --------- */
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.registerTermsAccepted,
                        onCheckedChange = {
                            viewModel.handleIntent(AuthIntent.RegisterTermsChanged(it))
                        },
                        colors          = CheckboxDefaults.colors(
                            checkedColor   = OrangeMain,
                            uncheckedColor = TextGrayMode
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        buildAnnotatedString {
                            append(stringResource(R.string.register_terms_part1))
                            withStyle(SpanStyle(color = OrangeMain, fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(R.string.register_terms_link1))
                            }
                            append(stringResource(R.string.register_terms_part2))
                            withStyle(SpanStyle(color = OrangeMain, fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(R.string.register_terms_link2))
                            }
                        },
                        fontSize = 13.sp,
                        color    = TextGrayMode
                    )
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text     = state.error!!,
                        color    = ErrorLight,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                /* ------------ Bouton S'inscrire --------- */
                AuthButton(
                    text = stringResource(R.string.register_button),
                    onClick   = {
                        viewModel.handleIntent(AuthIntent.SubmitRegister)
                    },
                    enabled   = state.registerName.isNotBlank()
                            && state.registerEmail.isNotBlank()
                            && !state.emailError
                            && state.registerPhone.isNotBlank()
                            && !state.phoneError
                            && state.registerPassword.isNotBlank()
                            && state.registerConfirmPassword.isNotBlank()
                            && !state.passwordMatchError
                            && state.registerTermsAccepted,
                    isLoading = state.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* ------------ Lien Connexion --------- */
                AuthFooterLink(
                    question   = stringResource(R.string.register_has_account),
                    actionText = stringResource(R.string.register_login_link),
                    onActionClick = onNavigateToLogin
                )
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    EventTicketAppTheme {
        RegisterScreen()
    }
}
*/