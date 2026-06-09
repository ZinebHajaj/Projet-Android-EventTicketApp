package com.dcc.eventticketapp.data.Api


import com.dcc.eventticketapp.data.Entities.EventModel
import retrofit2.http.GET

interface EventApi {
    @GET("events.json")
    suspend fun getEvents(): List<EventModel>
}