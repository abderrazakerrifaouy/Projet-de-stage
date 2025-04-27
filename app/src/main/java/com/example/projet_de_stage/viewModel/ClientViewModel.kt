package com.example.projet_de_stage.viewModel

import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.ShopRepository

class ClientViewModel : ViewModel() {
    private val shopRepository = ShopRepository()



    suspend fun getShops(): List<Shop> {
        return shopRepository.getAllShops()
    }

}