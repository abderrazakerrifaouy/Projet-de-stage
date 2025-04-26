package com.example.projet_de_stage.viewModel

import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.ShopRepository

class BarberViewModel : ViewModel() {
    private val shopRepository = ShopRepository()


    suspend fun getShopByBarber(barber: Barber): Shop? {
        return shopRepository.getAllShops()
            .firstOrNull { shop -> barber in shop.barbers }
    }

}