package com.dcc.eventticketapp.ui.auth

import com.dcc.eventticketapp.data.Entities.User

data class AuthViewState(

    // État général
    val isLoading       : Boolean = false,
    val user            : User?   = null,
    val error           : String? = null,
    val isAuthenticated : Boolean = false,
    val isSuccess       : Boolean = false,

    // Champs Login
    val loginEmail    : String = "",
    val loginPassword : String = "",

    // Champs Register
    val registerName            : String  = "",
    val registerEmail           : String  = "",
    val registerPhone           : String  = "",
    val registerPassword        : String  = "",
    val registerConfirmPassword : String  = "",
    val registerTermsAccepted   : Boolean = false,

    // Erreurs validation
    val emailError         : Boolean = false,
    val phoneError         : Boolean = false,
    val passwordMatchError : Boolean = false,

    //Role
    val userRole: String = "user"
)