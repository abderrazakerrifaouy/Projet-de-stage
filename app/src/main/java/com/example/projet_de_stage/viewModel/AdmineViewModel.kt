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
import kotlinx.coroutines.launch

/**
 * ViewModel for admin functionalities, handling shop, barber, appointment, and join request operations.
 */
class AdminViewModel : ViewModel() {

    // Repositories to interact with the data layer
    private val repositoryJoinRequests = JoinRequestRepository()
    private val shopRepository = ShopRepository()
    private val shopOwnerRepository = ShopOwnerRepository()
    private val barberRepository = BarberRepository()
    private val appointmentRepository = AppointmentRepository()
    private val clientRepository = CustomerRepository()

    // LiveData to hold data
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


    /**
     * Fetch the shop owner by ID.
     */
    fun getShopOwnerById(id: String, callback: (ShopOwner?) -> Unit) {
        shopOwnerRepository.getShopOwnerById(
            id = id,
            onSuccess = { shopOwner ->
                callback(shopOwner)
            },
            onFailure = { exception ->
                callback(null)
            }
        )
    }


    /**
     * Create a new shop and upload its image.
     */
    fun createShop(shop: Shop, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            try {
                shopRepository.createShopWithImage(
                    shop, imageUri, context,
                    onSuccess = {
                        _shopCreationStatus.value = true
                    },
                    onFailure = { exception ->
                        _errorMessage.value = "Error creating shop: ${exception.message}"
                        _shopCreationStatus.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                _shopCreationStatus.value = false
            }
        }
    }

    /**
     * Suspend function to get shops by owner's ID.
     */
    suspend fun getShopsByOwnerIdSuspend(ownerId: String): List<Shop> {
        return try {
            val allShops = shopRepository.getAllShops()
            allShops.filter { it.ownerId == ownerId }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Fetch join requests for a shop owner by ID.
     */
    fun getJoinRequestsByShopOwnerId(shopOwnerId: String) {
        viewModelScope.launch {
            repositoryJoinRequests.getRequestsByShopOwnerId(
                shopOwnerId,
                onSuccess = { requests ->
                    _joinRequests.postValue(requests)
                },
                onFailure = { exception ->
                    _errorMessage.postValue("Failed to fetch requests: ${exception.message}")
                }
            )
        }
    }

    /**
     * Fetch barber by ID.
     */
    fun getBarberById(id: String) {
        barberRepository.getBarberById(
            id,
            onSuccess = { barber ->
                _barber.postValue(barber)
            },
            onFailure = { exception ->
                _errorMessage.postValue("Failed to fetch barber: ${exception.message}")
            }
        )
    }

    /**
     * Add a barber to a shop.
     */
    fun addBarberToShop(shopId: String, barber: Barber) {
        viewModelScope.launch {
            try {
                shopRepository.addBarberToShop(shopId, barber,
                    onSuccess = {
                        _barber.postValue(barber)
                    },
                    onFailure = { exception ->
                        _errorMessage.postValue("Failed to add barber: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }

    /**
     * Fetch appointments for a shop owner by ID.
     */
    fun getAppointmentsByShopOwnerId(shopOwnerId: String) {
        viewModelScope.launch {
            try {
                val shopsList = shopRepository.getAllShops()
                val shopIds = shopsList.filter { it.ownerId == shopOwnerId }.map { it.id }

                val allAppointments = mutableListOf<Appointment>()

                shopIds.forEach { shopId ->
                    appointmentRepository.getAppointmentsByShopId(
                        shopId,
                        onSuccess = { appointments ->
                            allAppointments.addAll(appointments.filter { it.status == "pending" || it.status == "accepted" })
                            _appointments.postValue(allAppointments.toList()) // Update LiveData
                        },
                        onFailure = { exception ->
                            _errorMessage.postValue("Failed to fetch appointments: ${exception.message}")
                        }
                    )
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Unexpected error: ${e.message}")
            }
        }
    }

    /**
     * Load customer details by ID.
     */
    fun loadCustomerById(id: String) {
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

    /**
     * Update the status of an appointment.
     */
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

    /**
     * Update the status of a join request.
     */
    fun updateJoinRequestStatus(
        requestId: String,
        newStatus: String,
        onSuccess: (Boolean) -> Unit
    ) {
        repositoryJoinRequests.updateRequestStatus(
            requestId = requestId,
            newStatus = newStatus,
            onSuccess = { onSuccess(it) } ,
            onFailure = { _error.postValue(it.message) }
        )
    }


    /**
     * Delete a barber from a shop.
     */

    fun deleteBarberFromShop(
        shopId: String,
        barberId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
        ) {
        shopRepository.deleteBarberFromShop(
            shopId = shopId,
            barberId = barberId,
            onSuccess = { onSuccess() },
            onFailure = { e -> onFailure(e) }
        )
        appointmentRepository.deleteRequestByShopIdAndBarberId(
            shopId = shopId,
            barberId = barberId,
            onSuccess = { onSuccess() },
            onFailure = { e -> onFailure(e) }
        )
    }
}
