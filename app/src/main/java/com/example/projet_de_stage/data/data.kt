package com.example.projet_de_stage.data

import java.time.LocalDate

data class Appointment(
    val id: String,
    val clientName: String,
    val time: String,
    val service: String,
    var status: String ,
    val date: LocalDate
)
data class JoinRequest(
    val id: String,
    val name: String,
    val experience: String ,
    val Email: String ,
    val Phone: String ,
    val Address: String ,
    val Password: String
)
data class Shop(
    val id: String,
    val name: String,
    val rating: String,
    val reviews: String,
    val imageRes: Int
)

data class Rating(
    val id: String,
    val shopName: String,
    val customer: String,
    val rating: String,
    val comment: String,
    val date: String
)

