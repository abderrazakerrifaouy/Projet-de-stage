package com.example.projet_de_stage.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.AppointmentRepository
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.repository.CustomerRepository
import com.example.projet_de_stage.repository.ShopRepository

/**
 * ViewModel for managing client-related data and actions.
 */
class ClientViewModel : ViewModel() {

    private val clientRepository = CustomerRepository()
    private val shopRepository = ShopRepository()
    private val appointmentRepository = AppointmentRepository()
    private val barberRepository = BarberRepository()

    // LiveData for shops
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    // LiveData for selected barber
    private val _barber = MutableLiveData<Barber?>()
    val barber: LiveData<Barber?> = _barber

    // LiveData for selected shop
    private val _shop = MutableLiveData<Shop?>()
    val shop: LiveData<Shop?> = _shop

    // LiveData for appointments
    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    private val _appointmentsDate = MutableLiveData<List<Appointment>>()
    val appointmentsDate: LiveData<List<Appointment>> = _appointmentsDate

    // LiveData for errors
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Retrieves all shops and updates LiveData.
     * @return List of all shops.
     */
    suspend fun getShops(): List<Shop> {
        return shopRepository.getAllShops()
    }

    /**
     * Adds a new appointment and responds to success or failure.
     * @param appointment The appointment data to add.
     * @param onSuccess Callback for success.
     * @param onFailure Callback for failure.
     */
    fun addAppointment(
        appointment: Appointment,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        appointmentRepository.addAppointment(
            appointment = appointment,
            onSuccess = { onSuccess() },
            onFailure = { e -> onFailure(e) })
    }

    /**
     * Updates customer data in the repository.
     * @param customer The customer data to update.
     * @param onSuccess Callback for success.
     * @param onFailure Callback for failure.
     */
    fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        clientRepository.updateCustomer(
            customer = customer,
            onSuccess = {
                onSuccess()
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
                onFailure(exception)
            }
        )
    }

    /**
     * Retrieves appointments for a customer and updates LiveData.
     * @param customerId The ID of the customer.
     */
    fun fetchAppointmentsByCustomerId(customerId: String) {
        appointmentRepository.getAppointmentsByCustomerId(
            customerId = customerId,
            onSuccess = { list ->
                _appointments.postValue(list)
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
            }
        )
    }

    /**
     * Retrieves appointments for a barber by barber ID and status, then updates LiveData.
     * @param barberId The ID of the barber.
     */
    fun getAppointmentsByBarberId(
        barberId: String,
    ) {
        appointmentRepository.getAllAppointmentsByBarberIdandStatus(
            status = "accepted",
            barberId = barberId,
            onSuccess = { list ->
                _appointmentsDate.postValue(list)
                Log.e("Firestore", "Failed to fetch appointments ${list.size}")
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
                Log.e("FirestoreError", "Failed to fetch appointments", exception)
            }
        )
    }

    /**
     * Retrieves barber data by barber ID.
     * @param id The ID of the barber.
     */
    fun getBarberById(
        id: String,
    ) {
        barberRepository.getBarberById(
            id = id,
            onSuccess = { barber ->
                _barber.postValue(barber)
            },
            onFailure = { e ->
                _error.postValue(e.message)
            })
    }

    /**
     * Retrieves shop data by shop ID.
     * @param id The ID of the shop.
     */
    fun getShopById(
        id: String,
    ) {
        shopRepository.getShopById(
            id = id,
            onSuccess = { shop ->
                _shop.postValue(shop)
            },
            onFailure = { e ->
                _error.postValue(e.message)
            })
    }
}
