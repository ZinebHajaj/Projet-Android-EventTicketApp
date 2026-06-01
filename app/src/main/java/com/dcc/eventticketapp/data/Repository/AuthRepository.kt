package com.dcc.eventticketapp.data.Repository

import com.dcc.eventticketapp.data.Entities.User
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    suspend fun login(
        email    : String,
        password : String
    ): User {
        delay(1500)
        return User(
            userId   = "USR001",
            fullName = "Mohamed Ali",
            email    = email,
            phone    = "0612345678"
        )
    }

    suspend fun register(
        fullName : String,
        email    : String,
        phone    : String,
        password : String
    ): User {
        delay(1500)
        return User(
            userId   = "USR002",
            fullName = fullName,
            email    = email,
            phone    = phone
        )
    }
}