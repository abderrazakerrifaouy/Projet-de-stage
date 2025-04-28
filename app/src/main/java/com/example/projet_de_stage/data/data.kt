package com.example.projet_de_stage.data

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.example.projet_de_stage.R
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

// User types
@Parcelize
data class ShopOwner(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    val phone: String = "",
    val address: String = "",
    var password: String = "",
    val shopId: String= "",
    val imageRes: Int = R.drawable.my_profile,
    var birthDate: String = ""
): Parcelable

@Parcelize
data class Barber(
    var uid: String = "",
    var name: String = "",
    val experience: String = "",
    var email: String = "",
    val phone: String = "",
    var password: String = "",
    var birthDate: String = "",
    val shopId: String = "",
    val imageRes: Int = R.drawable.my_profile,
    val rating: Float = 0f ,
    val Nrating: Int = 0
): Parcelable

@Parcelize
data class Customer(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    val phone: String = "",
    var birthDate: String = "",
    val address: String = "",
    var password: String = "",
    val imageRes: Int = R.drawable.my_profile,
): Parcelable

@RequiresApi(Build.VERSION_CODES.O)
data class Appointment (
    val id: String = "",
    val clientName: String = "",
    val time: String = "",
    val service: String = "",
    var status: String = "",  // "مكتمل", "ملغي", "قيد الانتظار", etc.
    val date: LocalDate = LocalDate.now(),
    val shopId: String = "",
    val barberId: String = ""
)

data class JoinRequest(
    val id: String = "",
    val experience: String = "",
    val idBarber: String = "",
    val idShop: String = "",
    var date: String = "" ,
    val status: String = "" ,
    val idShopOwner: String = ""
)
@Parcelize
data class Shop(
    val id: String = "",
    val name: String = "",
    val idOwner: String = "",
    val address: String = "",
    val nbarbers: Int = 3,
    val imageRes: Int = R.drawable.my_profile,
    val imageUrl: String = "", // ← هذا الجديد
    val barbers: List<Barber> = emptyList()
) : Parcelable


data class Rating(
    val id: String = "",
    val chopId: String = "",
    val barberName: String = "",
    val customer: String = "",
    val rating: String = "",
    val comment: String = "",
    val date: String = ""
)