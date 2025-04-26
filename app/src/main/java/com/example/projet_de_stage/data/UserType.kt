package com.example.projet_de_stage.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
enum class UserType : Parcelable {
     ShopOwner ,
     Barber ,
     Customer
}

