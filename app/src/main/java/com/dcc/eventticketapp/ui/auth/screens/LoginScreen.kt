package com.dcc.eventticketapp.ui.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.eventticketapp.R
import com.dcc.eventticketapp.ui.theme.DividerColor
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

import com.dcc.eventticketapp.ui.auth.components.AuthTextField
import com.dcc.eventticketapp.ui.auth.components.AuthButton
import com.dcc.eventticketapp.ui.auth.components.AuthFooterLink
import com.dcc.eventticketapp.ui.auth.components.AuthHeader
import com.dcc.eventticketapp.ui.auth.components.SsoButton

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {},
    onFacebookSignInClick: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

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
                ),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                /* ------------ Header --------- */
                AuthHeader(
                    title    = stringResource(R.string.login_title),
                    subtitle = stringResource(R.string.login_subtitle)
                )

                Spacer(modifier = Modifier.height(30.dp))

                /* ------------ Champ Email --------- */
                AuthTextField(
                    value         = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.login_email),
                    leadingIcon   = Icons.Default.Email,
                    keyboardType  = KeyboardType.Email
                )

                /* ------------ Champ Password --------- */
                AuthTextField(
                    value              = password,
                    onValueChange      = { password = it },
                    label = stringResource(R.string.login_password),
                    leadingIcon        = Icons.Default.Lock,
                    isPassword         = true,
                    passwordVisible    = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )

                /* ------------ Mot de passe oublié --------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.login_forgot_password),
                        color      = OrangeMain,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier   = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                /* ------------ Bouton Connexion --------- */
                AuthButton(
                    text = stringResource(R.string.login_button),
                    onClick   = { isLoading = true; onLoginClick() },
                    enabled   = email.isNotBlank() && password.isNotBlank(),
                    isLoading = isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                /* ------------ Séparateur --------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
                    Text(
                        text = stringResource(R.string.login_or),
                        modifier = Modifier.padding(horizontal = 14.dp),
                        style    = MaterialTheme.typography.bodySmall,
                        color    = TextGrayMode
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
                }

                Spacer(modifier = Modifier.height(20.dp))

                /* ------------ Boutons SSO --------- */
                SsoButton(
                    text    = "Sign in with Google",
                    icon    = painterResource(id = R.drawable.logo_gogle),
                    onClick = onGoogleSignInClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                SsoButton(
                    text = stringResource(R.string.login_facebook),
                    icon    = painterResource(id = R.drawable.logo_facebook),
                    onClick = onFacebookSignInClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* ------------ Lien S'inscrire --------- */
                AuthFooterLink(
                    question      = stringResource(R.string.login_no_account),
                    actionText    = stringResource(R.string.login_register_link),
                    onActionClick = onNavigateToRegister
                )
            }

        }
    }
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    EventTicketAppTheme() {
        LoginScreen()
    }
}
