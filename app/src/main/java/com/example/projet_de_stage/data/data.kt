package com.example.projet_de_stage.data

data class Appointment(val id: String, val clientName: String, val time: String, val service: String, val status: String)
data class JoinRequest(val id: String, val name: String, val experience: String)
data class Shop(val id: String, val name: String, val rating: String, val reviews: String, val imageRes: Int)
data class Rating(val id: String, val shopName: String, val customer: String, val rating: String, val comment: String, val date: String)
