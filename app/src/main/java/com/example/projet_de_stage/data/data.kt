package com.example.projet_de_stage.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

// User types
data class ShopOwner(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val shopId: String
)

@Parcelize
data class Barber(
    val id: String,
    val name: String,
    val experience: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val shopId: String,
    val imageRes: Int ,
    val rating: Float = 0f
): Parcelable

@Parcelize
data class Customer(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val imageRes: Int
): Parcelable

data class Appointment(
    val id: String,
    val clientName: String,
    val time: String,
    val service: String,
    var status: String,  // "مكتمل", "ملغي", "قيد الانتظار", etc.
    val date: LocalDate,
    val shopId: String ,
    val barberId: String
)

data class JoinRequest(
    val id: String,
    val name: String,
    val experience: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String
)
@Parcelize
data class Shop(
    val id: String,
    val name: String,
    val rating: String,
    val reviews: String,
    val address: String,
    val imageRes: Int,
    val barbers: List<Barber> = emptyList() // قائمة الحلاقين في المحل
): Parcelable

data class Rating(
    val id: String,
    val shopName: String,
    val customer: String,
    val rating: String,
    val comment: String,
    val date: String
)