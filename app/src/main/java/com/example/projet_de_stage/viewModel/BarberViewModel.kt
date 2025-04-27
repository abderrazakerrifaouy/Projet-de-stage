package com.example.projet_de_stage.viewModel

import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.JoinRequestRepository
import com.example.projet_de_stage.repository.ShopRepository

class BarberViewModel : ViewModel() {
    private val shopRepository = ShopRepository()
    private val repositoryJoinRequests = JoinRequestRepository()



    suspend fun getShopByBarber(barber: Barber): Shop? {
        return shopRepository.getAllShops()
            .firstOrNull { shop -> barber in shop.barbers }
    }

    suspend fun getAllShops(): List<Shop> {
        return shopRepository.getAllShops()
    }

    fun createJoinRequests(request: JoinRequest, callback: (Boolean) -> Unit) {
        repositoryJoinRequests.addRequest(
            request,
            onSuccess = {
                callback(true)
            },
            onFailure = {
                callback(false)
            }
        )
    }

}