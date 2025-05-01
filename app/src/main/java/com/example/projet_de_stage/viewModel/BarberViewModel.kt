package com.example.projet_de_stage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.AppointmentRepository
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.repository.CustomerRepository
import com.example.projet_de_stage.repository.JoinRequestRepository
import com.example.projet_de_stage.repository.ShopRepository

class BarberViewModel : ViewModel() {
    private val shopRepository = ShopRepository()
    private val clientRepository = CustomerRepository()
    private val repositoryJoinRequests = JoinRequestRepository()
    private val appointmentsRepository = AppointmentRepository()
    private val barberRepository = BarberRepository()

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments


    private val _realTimeAppointments = MutableLiveData<List<Appointment>>()
    val realTimeAppointments: LiveData<List<Appointment>> = _realTimeAppointments

    // LiveData بيانات العميل
    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> = _customer

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error



    suspend fun getShopByBarber(barber: Barber): Shop? {
        return shopRepository.getAllShops()
            .firstOrNull { shop -> barber in shop.barbers }
    }

    suspend fun getShopByBarberId(barberId: String): Shop? {
        return shopRepository.getAllShops()
            .firstOrNull {
                it.barbers.any { barber -> barber.uid == barberId }
            }
    }

    fun updateShop(shop: Shop, callback: (Boolean) -> Unit) {
        shopRepository.updateShop(
            shop,
            onSuccess = {
                callback(true)
            },
            onFailure = {
                callback(false)
            }
        )
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

    fun getAppointmentByBarberIdandStatus(
        status: String,
        barberId: String
    )
    {
        appointmentsRepository.getAllAppointmentsByBarberIdandStatus(
            status = status,
            barberId = barberId,
            onSuccess = { list ->
                _appointments.postValue(list)
                _error.postValue(null)
            } ,
            onFailure = { exceptoin ->
                _error.postValue(exceptoin.message)
            }
        )
    }

    /**
     * يجلب بيانات العميل حسب المعرف ويحدث LiveData
     */
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

    fun getAllAppointmentsByBarberId(
        barberId: String
    ) {
        appointmentsRepository.getAllAppointmentsByBarberId(
            barberId = barberId,
            onSuccess = { list ->
                _appointments.postValue(list)
                _error.postValue(null)
            } ,
            onFailure = { exceptoin ->
                _error.postValue(exceptoin.message)
            }
        )
    }

    fun modifyBarber(barber: Barber, callback: (Boolean) -> Unit) {
        barberRepository.updateBarber(
            barber,
            onSuccess = {
                callback(true)
            },
            onFailure = {
                callback(false)
            }
        )

    }

    fun updateAppointmentStatus(
        appointmentId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        appointmentsRepository.updateAppointmentStatus(
            appointmentId = appointmentId,
            newStatus = newStatus,
            onSuccess = { onSuccess() },
            onFailure = { e -> onFailure(e) })
    }


    fun listenToNewRealtimeAppointments(
        barberId: String,
        onNewAppointment: (Appointment) -> Unit,
        onError: (String) -> Unit
    ) {
        appointmentsRepository.listenToNewAppointmentsForBarber(
            barberId = barberId,
            onNewAppointment = onNewAppointment,
            onError = { dbError -> onError(dbError.message) }
        )
    }

    fun deleteAppointmentInRealtimeDatabase(
        appointmentId: String ,
        onSuccess: (Boolean) -> Unit){
        appointmentsRepository.deleteAppointmentInRealtimeDatabase(
            appointmentId = appointmentId,
            onSuccess = { onSuccess(true) },
            onFailure = { onSuccess(false) }
        )
    }




}