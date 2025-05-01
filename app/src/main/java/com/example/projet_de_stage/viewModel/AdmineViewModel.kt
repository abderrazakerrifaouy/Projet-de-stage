package com.example.projet_de_stage.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.repository.AppointmentRepository
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.repository.CustomerRepository
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
    private val appointmentRepository = AppointmentRepository()
    private val clientRepository = CustomerRepository()

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> = _customer

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

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> get() = _appointments

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


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


    fun addBarberToShop(shopId: String, barber: Barber) {
        viewModelScope.launch {
            try {
                shopRepository.addBarberToShop(shopId, barber,
                    onSuccess = {
                        // تحديث حالة النجاح
                        _barber.postValue(barber)
                    },
                    onFailure = { exception ->
                        _errorMessage.postValue("فشل في إضافة الحلاق: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _errorMessage.postValue("خطأ: ${e.message}")
            }
        }
    }

    fun getAppointmentsByShopOwnrId(
        shopOwnerId: String
    ) {
        viewModelScope.launch {
            try {
                val list = shopRepository.getAllShops()
                val listShopId = list.filter { it.idOwner == shopOwnerId }.map { it.id }

                val allAppointments = mutableListOf<Appointment>()

                listShopId.forEach { shopId ->
                    appointmentRepository.getAppointmentsByShopId(
                        shopId,
                        onSuccess = { appointments ->
                            allAppointments.addAll(appointments.filter { it.status == "pending" || it.status == "accepted" })
                            _appointments.postValue(allAppointments.toList()) // تحديث الـ LiveData
                        },
                        onFailure = { exception ->
                            _errorMessage.postValue("فشل في جلب المواعيد: ${exception.message}")
                        }
                    )
                }
            } catch (e: Exception) {
                _errorMessage.postValue("خطأ غير متوقع: ${e.message}")
            }
        }
    }


     fun loadCustomerById(
        id: String,
    ) {
        clientRepository.getCustomerById(
            id = id,
            onSuccess = { c ->
                _customer.postValue(c)
                _error.postValue(null)
            },
            onFailure = { e ->
                _error.postValue(e.message)
            }
        )
    }


    fun updateAppointmentStatus(
        appointmentId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        appointmentRepository.updateAppointmentStatus(
            appointmentId = appointmentId,
            newStatus = newStatus,
            onSuccess = { onSuccess() },
            onFailure = { e -> onFailure(e) })
    }

    fun updateJoinRequestStatus(
        requestId: String,
        newStatus: String,
        onSuccess: (Boolean) -> Unit,
    ) {
        repositoryJoinRequests.updateRequestStatus(
            requestId = requestId,
            newStatus = newStatus,
            onSuccess = { onSuccess(it) }
        )
    }


}
