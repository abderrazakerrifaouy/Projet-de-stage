package com.example.projet_de_stage.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.repository.JoinRequestRepository
import com.example.projet_de_stage.repository.ShopOwnerRepository
import com.example.projet_de_stage.repository.ShopRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdmineViewModel : ViewModel() {
    private val repositoryJoinRequests = JoinRequestRepository()
    private val shopRepository = ShopRepository()
    private val shopOwnerRepository = ShopOwnerRepository()
    private val barberRepository = BarberRepository()

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    private val _shopCreationStatus = MutableLiveData<Boolean>()
    val shopCreationStatus: LiveData<Boolean> = _shopCreationStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _shopOwner = MutableLiveData<ShopOwner?>()
    val shopOwner: LiveData<ShopOwner?> get() = _shopOwner

    private val _joinRequests = MutableLiveData<List<JoinRequest>>()
    val joinRequests: LiveData<List<JoinRequest>> get() = _joinRequests


    private val _barber = MutableLiveData<Barber?>()
    val barber: LiveData<Barber?> get() = _barber


    fun getShopOwnerById(id: String, callback: (ShopOwner?) -> Unit) {
        shopOwnerRepository.getShopOwnerById(id, callback)
    }

    // دالة لإنشاء المحل
    fun createShop(shop: Shop, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            try {
                shopRepository.createShopWithImage(
                    shop, imageUri, context,
                    onSuccess = {
                        _shopCreationStatus.value = true
                    },
                    onFailure = { exception ->
                        _errorMessage.value = "خطأ في إنشاء المحل: ${exception.message}"
                        _shopCreationStatus.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "خطأ: ${e.message}"
                _shopCreationStatus.value = false
            }
        }
    }

    suspend fun getShopsByOwnerIdSuspend(ownerId: String): List<Shop> {
        return try {
            val allShops = shopRepository.getAllShops()
            allShops.filter { it.idOwner == ownerId }
        } catch (e: Exception) {
            emptyList()
        }
    }


    fun getJoinRequestsByShopOwnerId(shopOwnerId: String) {
        viewModelScope.launch {
            repositoryJoinRequests.getRequestsByShopOwnerIdId(
                shopOwnerId,
                onSuccess = { requests ->
                    _joinRequests.postValue(requests)
                },
                onFailure = { exception ->
                    _errorMessage.postValue("فشل في جلب الطلبات: ${exception.message}")
                }
            )
        }
    }

    fun getBarberById(id: String) {
        barberRepository.getBarberById(
            id,
            onSuccess = { barber ->
                _barber.postValue(barber)
            },
            onFailure = { exception ->
                _errorMessage.postValue("فشل في جلب الببر: ${exception.message}")
            }
        )
    }




}
