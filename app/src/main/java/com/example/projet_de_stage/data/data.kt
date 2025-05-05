package com.example.projet_de_stage.data

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.example.projet_de_stage.R
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

/**
 * Represents a shop owner user.
 */
@Parcelize
data class ShopOwner(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    val phone: String = "",
    val address: String = "",
    var password: String = "",
    val shopId: String = "",
    val imageRes: Int = R.drawable.my_profile,
    var birthDate: String = ""
) : Parcelable

/**
 * Represents a barber user with experience and rating.
 */
@Parcelize
data class Barber(
    var uid: String = "",
    var name: String = "",
    var experience: String = "",
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var birthDate: String = "",
    val shopId: String = "",
    val imageRes: Int = R.drawable.my_profile,
    val rating: Float = 0f,
    val numberOfRatings: Int = 0,
    var imageUrl: String = ""
) : Parcelable

/**
 * Represents a customer user.
 */
@Parcelize
data class Customer(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var birthDate: String = "",
    var address: String = "",
    var password: String = "",
    val imageRes: Int = R.drawable.my_profile ,
    val imageUrl: String = ""
) : Parcelable

/**
 * Represents an appointment made by a customer with a barber at a specific shop.
 */
@RequiresApi(Build.VERSION_CODES.O)
data class Appointment(
    val id: String = "",
    val clientId: String = "",
    val time: String = "",
    val service: String = "",
    var status: String = "", // Options: pending, accepted, rejected, canceled, completed
    val date: String = LocalDate.now().plusDays(1).toString(), // Default is tomorrow
    val shopId: String = "",
    val barberId: String = ""
)

/**
 * Represents a request by a barber to join a specific shop.
 */
data class JoinRequest(
    val id: String = "",
    val experience: String = "",
    val barberId: String = "", // Renamed from 'idBarber'
    val shopId: String = "",   // Renamed from 'idShop'
    var date: String = "",
    val status: String = "", // Options: pending, accepted
    val shopOwnerId: String = "" // Renamed from 'idShopOwner'
)

/**
 * Represents a barber shop with owner and list of barbers.
 */
@Parcelize
data class Shop(
    val id: String = "",
    val name: String = "",
    val ownerId: String = "", // Renamed from 'idOwner'
    val address: String = "",
    val numberOfBarbers: Int = 3, // Renamed from 'nbarbers'
    val imageRes: Int = R.drawable.my_profile,
    val imageUrl: String = "",
    val barbers: MutableList<Barber> = mutableListOf()
) : Parcelable

/**
 * Represents a rating left by a customer for a barber.
 */
data class Rating(
    val id: String = "",
    val shopId: String = "", // Fixed typo from 'chopId'
    val barberName: String = "",
    val customerName: String = "", // Renamed from 'customer'
    val ratingValue: String = "", // Renamed for clarity
    val comment: String = "",
    val date: String = ""
)
