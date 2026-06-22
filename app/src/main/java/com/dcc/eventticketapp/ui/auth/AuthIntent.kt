package com.dcc.eventticketapp.ui.auth

import com.facebook.AccessToken

sealed class AuthIntent {

    // Login
    data class LoginEmailChanged(val email: String)       : AuthIntent()
    data class LoginPasswordChanged(val password: String) : AuthIntent()
    object SubmitLogin                                    : AuthIntent()

    // Register
    data class RegisterNameChanged(val name: String)                       : AuthIntent()
    data class RegisterEmailChanged(val email: String)                     : AuthIntent()
    data class RegisterPhoneChanged(val phone: String)                     : AuthIntent()
    data class RegisterPasswordChanged(val password: String)               : AuthIntent()
    data class RegisterConfirmPasswordChanged(val confirmPassword: String)  : AuthIntent()
    data class RegisterTermsChanged(val accepted: Boolean)                 : AuthIntent()
    object SubmitRegister                                                  : AuthIntent()

    // Commun
    object ResetState : AuthIntent()

    //Deconnexion
    object Logout : AuthIntent()

    // Change session
    object CheckSession : AuthIntent()

    // SSO Google
    data class GoogleSignInResult(val idToken: String) : AuthIntent()
    object GoogleSignInError : AuthIntent()

    // SSO Facebook
    data class FacebookSignInResult(val accessToken: AccessToken) : AuthIntent()
    object FacebookSignInError : AuthIntent()

    object ResetSuccess : AuthIntent()
}